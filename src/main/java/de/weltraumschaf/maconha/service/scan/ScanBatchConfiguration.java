package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.maconha.DatabaseConfiguration;
import de.weltraumschaf.maconha.service.ScanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
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

    @Autowired
    ScanBatchConfiguration(final JobBuilderFactory jobs, final StepBuilderFactory steps) {
        super();
        this.jobs = jobs;
        this.steps = steps;
    }

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
            .tasklet(new FindFilesTasklet())
            .build();
    }

    @Bean
    public Step filterSeenFilesStep() {
        LOGGER.debug("Create FilterSeenFilesStep bean.");
        return steps.get("FilterSeenFilesStep")
            .tasklet(new FilterSeenFilesTasklet())
            .build();
    }

    @Bean
    public Step metaDataExtractionStep() {
        LOGGER.debug("Create MetaDataExtractionStep bean.");
        return steps.get("MetaDataExtractionStep")
            .tasklet(new MetaDataExtractionTasklet())
            .build();
    }
}
