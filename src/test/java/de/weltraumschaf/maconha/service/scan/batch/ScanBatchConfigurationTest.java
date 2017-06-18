package de.weltraumschaf.maconha.service.scan.batch;

import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.repo.BucketRepo;
import de.weltraumschaf.maconha.service.MediaFileService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ScanBatchConfiguration}.
 */
public final class ScanBatchConfigurationTest {
    private final ScanJobExecutionListener listener = new ScanJobExecutionListener();
    private final MaconhaConfiguration config = new MaconhaConfiguration();
    private final JobBuilderFactory jobBuilders = mock(JobBuilderFactory.class);
    private final StepBuilderFactory stepBuilders = mock(StepBuilderFactory.class);

    private final ScanBatchConfiguration sut = new ScanBatchConfiguration(listener, jobBuilders, stepBuilders, config);

    @Before
    public void injectDependenciesToSut() {
        sut.setJobs(mock(JobRepository.class));
        sut.setBuckets(mock(BucketRepo.class));
        sut.setMediaFiles(mock(MediaFileService.class));
    }

    @Test
    public void taskExecutor() {
        final TaskExecutor taskExecutor = sut.taskExecutor();
        final Class expectedType = SimpleAsyncTaskExecutor.class;

        //noinspection unchecked
        assertThat(taskExecutor, isA(expectedType));
        assertThat(((SimpleAsyncTaskExecutor)taskExecutor).getConcurrencyLimit(), is(10));
    }

}
