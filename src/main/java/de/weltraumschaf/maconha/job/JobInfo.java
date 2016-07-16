package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.job.Job.State;
import java.util.Objects;
import org.joda.time.LocalDateTime;

/**
 * Describes a {@link Job}.
 */
public final class JobInfo {

    /**
     * Unique name of job.
     */
    private final String name;
    /**
     * State of the job.
     */
    private final State state;
    /**
     * Percentage of work done.
     */
    private final double progress;
    private final LocalDateTime startTime;

    /**
     * Dedicated constructor.
     *
     * @param name must not be {@code null} or empty
     * @param state must not be {@code null}
     * @param progress must not be negative
     * @param startTime must not be {@code null}
     */
    public JobInfo(final String name, final State state, final double progress, final LocalDateTime startTime) {
        super();
        this.name = Validate.notEmpty(name, "name");
        this.state = Validate.notNull(state, "state");

        if (progress < 0) {
            throw new IllegalArgumentException(
                String.format("Parameter progress must not be negative (Was %f)!", progress));
        }

        this.progress = progress;
        this.startTime = Validate.notNull(startTime, "startTime");
    }

    /**
     * Get the unique job name.
     *
     * @return never {@code null} or empty
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current state of the job.
     *
     * @return never {@code null}
     */
    public State getState() {
        return state;
    }

    /**
     * Get percentage of done work.
     *
     * @return between 0.0 and 1.0
     */
    public double getProgress() {
        return progress;
    }


    /**
     * Get the time of job started.
     *
     * @return never{@code null}
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "JobInfo{"
            + "name=" + name + ", "
            + "state=" + state + ", "
            + "progress=" + progress + ", "
            + "startTime=" + startTime
            + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, state, progress, startTime);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JobInfo)) {
            return false;
        }

        final JobInfo other = (JobInfo) obj;
        return Objects.equals(name, other.name)
            && Objects.equals(state, other.state)
            && Objects.equals(progress, other.progress)
            && Objects.equals(startTime, other.startTime);
    }

}
