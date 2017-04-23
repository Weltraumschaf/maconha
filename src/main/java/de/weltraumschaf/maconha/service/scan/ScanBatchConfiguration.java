package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.maconha.DatabaseConfiguration;
import de.weltraumschaf.maconha.repo.BucketRepo;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.ScanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

/**
 * This class configures the batch job for {@link ScanService scanning}.
 */
@Configuration
@Import(DatabaseConfiguration.class) // Provide a datasource for meta dta storage in database to make them persistent.
public class ScanBatchConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanBatchConfiguration.class);
    private static final int JOB_LIMIT = 10;

    private final JobExecutionListener listener = new JobExecutionListener() {
        @Override
        public void beforeJob(final JobExecution jobExecution) {
            LOGGER.debug("<<before job>>");
        }

        @Override
        public void afterJob(final JobExecution jobExecution) {
            LOGGER.debug("<<after job>>");
        }
    };

    private final JobBuilderFactory jobs;
    private final StepBuilderFactory steps;
    private final JobRepository repo;
    private final BucketRepo buckets;
    private final MediaFileRepo mediaFiles;
    private final KeywordRepo keywords;
    @Value("${bin.dir}")
    private String binDir;

    @Autowired
    ScanBatchConfiguration(final JobBuilderFactory jobs, final StepBuilderFactory steps, final JobRepository repo, final BucketRepo buckets, final MediaFileRepo mediaFiles, final KeywordRepo keywords) {
        super();
        this.jobs = jobs;
        this.steps = steps;
        this.repo = repo;
        this.buckets = buckets;
        this.mediaFiles = mediaFiles;
        this.keywords = keywords;
    }

    @Bean // Name must not be jobLauncher. See https://github.com/spring-projects/spring-boot/issues/1655
    public JobLauncher asyncJobLauncher() throws Exception {
        final SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(repo);
        launcher.setTaskExecutor(taskExecutor());
        launcher.afterPropertiesSet();
        LOGGER.debug("Created job launcher: {}", launcher);
        return launcher;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        final SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("spring_batch");
        taskExecutor.setConcurrencyLimit(JOB_LIMIT);
        return taskExecutor;
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        final ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[]{
            ContextKeys.UNSEEN_FILES
        });
        return listener;
    }

    @Bean(name = ScanService.JOB_NAME)
    public Job scanJob() {
        LOGGER.debug("Create {} bean.", ScanService.JOB_NAME);
        return jobs.get(ScanService.JOB_NAME)
            .listener(listener)
            .flow(findFilesStep())
            .next(filterSeenFilesStep())
            .next(metaDataExtractionStep())
            .end()
            .build();
    }

    @Bean
    public Step findFilesStep() {
        LOGGER.debug("Create FindFilesStep bean.");
        return steps.get("FindFilesStep")
            .tasklet(new FindFilesTasklet(binDir))
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public Step filterSeenFilesStep() {
        LOGGER.debug("Create FilterSeenFilesStep bean.");
        return steps.get("FilterSeenFilesStep")
            .tasklet(new FilterUnseenFilesTasklet(mediaFiles))
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public Step metaDataExtractionStep() {
        LOGGER.debug("Create MetaDataExtractionStep bean.");
        return steps.get("MetaDataExtractionStep")
            .tasklet(new MetaDataExtractionTasklet(buckets, mediaFiles, keywords))
            .allowStartIfComplete(true)
            .build();
    }

}
