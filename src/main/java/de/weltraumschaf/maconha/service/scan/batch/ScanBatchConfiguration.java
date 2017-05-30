package de.weltraumschaf.maconha.service.scan.batch;

import de.weltraumschaf.maconha.config.DatabaseConfiguration;
import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.repo.BucketRepo;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.MediaFileService;
import de.weltraumschaf.maconha.service.ScanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final ScanJobExecutionListener listener;
    private final JobBuilderFactory jobBuilders;
    private final StepBuilderFactory stepBuilders;
    private final MaconhaConfiguration config;

    private JobRepository jobs;
    private BucketRepo buckets;
    private MediaFileRepo mediaFileRepo;
    private KeywordRepo keywords;
    private MediaFileService mediaFiles;

    @Autowired
    public ScanBatchConfiguration(final ScanJobExecutionListener listener, final JobBuilderFactory jobBuilderss, final StepBuilderFactory stepBuilders, final MaconhaConfiguration config) {
        super();
        this.listener = listener;
        this.jobBuilders = jobBuilderss;
        this.stepBuilders = stepBuilders;
        this.config = config;
    }

    @Autowired
    public void setJobs(final JobRepository jobs) {
        this.jobs = jobs;
    }

    @Autowired
    public void setBuckets(final BucketRepo buckets) {
        this.buckets = buckets;
    }

    @Autowired
    public void setMediaFileRepo(final MediaFileRepo mediaFileRepo) {
        this.mediaFileRepo = mediaFileRepo;
    }

    @Autowired
    public void setKeywords(final KeywordRepo keywords) {
        this.keywords = keywords;
    }

    @Autowired
    void setMediaFiles(final MediaFileService mediaFiles) {
        this.mediaFiles = mediaFiles;
    }

    @Bean // Name must not be jobLauncher. See https://github.com/spring-projects/spring-boot/issues/1655
    public JobLauncher asyncJobLauncher() throws Exception {
        final SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobs);
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
        return jobBuilders.get(ScanService.JOB_NAME)
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
        return stepBuilders.get("FindFilesStep")
            .tasklet(new FindFilesTasklet(config.getBindir()))
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public Step filterSeenFilesStep() {
        LOGGER.debug("Create FilterSeenFilesStep bean.");
        return stepBuilders.get("FilterSeenFilesStep")
            .tasklet(new FilterUnseenFilesTasklet(mediaFiles))
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public Step metaDataExtractionStep() {
        LOGGER.debug("Create MetaDataExtractionStep bean.");
        return stepBuilders.get("MetaDataExtractionStep")
            .tasklet(new MetaDataExtractionTasklet(buckets, mediaFileRepo, keywords, mediaFiles))
            .allowStartIfComplete(true)
            .build();
    }

}
