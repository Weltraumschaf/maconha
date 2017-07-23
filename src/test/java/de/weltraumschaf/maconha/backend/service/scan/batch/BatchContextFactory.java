package de.weltraumschaf.maconha.backend.service.scan.batch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;

import java.util.Collections;
import java.util.Map;

/**
 * Helper to create context objects with its dependencies.
 */
final class BatchContextFactory {
    ChunkContext createContext() {
        return createContext(Collections.emptyMap());
    }

    ChunkContext createContext(final ExecutionContext executionContext) {
        return createContext(Collections.emptyMap(), executionContext);
    }

    ChunkContext createContext(final Map<String, JobParameter> parameters) {
        return createContext(parameters, new ExecutionContext());
    }

    ChunkContext createContext(final Map<String, JobParameter> parameters, final ExecutionContext executionContext) {
        final JobParameters jobParameters = new JobParameters(parameters);
        final JobExecution jobExecution = new JobExecution(new JobInstance(42L, "job"), 42L, jobParameters, "job");
        jobExecution.setExecutionContext(executionContext);
        final StepExecution stepExecution = new StepExecution("name", jobExecution);
        final StepContext stepContext = new StepContext(stepExecution);
        return new ChunkContext(stepContext);
    }

}
