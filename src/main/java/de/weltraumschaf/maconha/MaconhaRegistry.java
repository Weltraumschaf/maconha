package de.weltraumschaf.maconha;

import de.weltraumschaf.maconha.job.JobExecutor;

/**
 */
public final class MaconhaRegistry {

    public static final MaconhaRegistry INSTANCE = new MaconhaRegistry();
    private JobExecutor jobExecutor;

    private MaconhaRegistry() {
        super();
    }

    public JobExecutor getJobExecutor() {
        return jobExecutor;
    }

    public void setJobExecutor(final JobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }

}
