package de.weltraumschaf.maconha.job;

/**
 */
public final class ProgressMonitor {

    private int totalWork;
    private int worked;

    public void begin(final int totalWork) {
        if (totalWork < 1) {
            throw new IllegalArgumentException("Parameter totalWork must not be less than 1!");
        }

        this.totalWork = totalWork;
    }

    public void done() {
        worked = totalWork;
    }

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
