package de.weltraumschaf.maconha;

import de.weltraumschaf.maconha.service.ScanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * This class configures the batch job for {@link ScanService scanning}.
 */
@Configuration
@Import(DatabaseConfiguration.class)
public class ScanBatchConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanBatchConfiguration.class);

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

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

    @Bean(name = ScanService.JOB_NAME)
    public Job scanJob() {
        LOGGER.debug("Create {} bean.", ScanService.JOB_NAME);
        return jobs.get(ScanService.JOB_NAME)
            .incrementer(new RunIdIncrementer())
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
            .tasklet((contribution, chunkContext) -> {
                LOGGER.debug("<<FindFilesStep>>");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Step filterSeenFilesStep() {
        LOGGER.debug("Create FilterSeenFilesStep bean.");
        return steps.get("FilterSeenFilesStep")
            .tasklet((contribution, chunkContext) -> {
                LOGGER.debug("<<FilterSeenFilesStep>>");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Step metaDataExtractionStep() {
        LOGGER.debug("Create FetaDataExtractionStep bean.");
        return steps.get("FetaDataExtractionStep")
            .tasklet((contribution, chunkContext) -> {
                LOGGER.debug("<<FilterSeenFilesStep>>");
                return RepeatStatus.FINISHED;
            })
            .build();
    }
}
