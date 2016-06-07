package de.weltraumschaf.maconha.job;

/**
 * This class tracks progress of a long running task.
 * <p>
 * The idea is to set a total amount of work and then repetitive tell the monitor how much work is done yet from the
 * long running task. Then any other client can ask the monitor how much the progress is.
 * </p>
 */
public final class ProgressMonitor {

    /**
     * Total amount of work.
     */
    private int totalWork;
    /**
     * The actual done work.
     */
    private int worked;

    /**
     * Call this method initial to set the total amount of work to be done.
     *
     * @param totalWork must not be less than 1
     */
    public void begin(final int totalWork) {
        if (totalWork < 1) {
            throw new IllegalArgumentException("Parameter totalWork must not be less than 1!");
        }

        this.totalWork = totalWork;
    }

    /**
     * This set the progress monitor done which means {@link #progress()} will return 1.0.
     */
    public void done() {
        worked = totalWork;
    }

    /**
     * Add the amount of done work until the toal amount set by {@link #begin(int)} is reached.
     *
     * @param work must not be less than 1
     */
    public void worked(final int work) {
        if (work < 1) {
            throw new IllegalArgumentException("Parameter work must not be less than 1!");
        }

        worked = Math.min(totalWork, worked + work);
    }

    /**
     * Returns percentage of worked.
     *
     * @return between 0 and 1
     */
    public double progress() {
        if (0 == totalWork) {
            return 0;
        }

        if (worked == totalWork) {
            return 1;
        }

        return 100d / (double) totalWork * (double) worked * 0.01d;
    }
}
