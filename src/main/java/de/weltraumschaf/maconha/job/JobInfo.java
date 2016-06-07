package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.job.Job.State;
import java.util.Objects;

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
     * Dedicated constructor.
     *
     * @param name must not be {@code null} or empty
     * @param state must not be {@code null}
     */
    public JobInfo(final String name, final State state) {
        super();
        this.name = Validate.notEmpty(name, "name");
        this.state = Validate.notNull(state, "state");
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

    @Override
    public String toString() {
        return "JobInfo{" + "name=" + name + ", state=" + state + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, state);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JobInfo)) {
            return false;
        }

        final JobInfo other = (JobInfo) obj;
        return Objects.equals(name, other.name)
            && Objects.equals(state, other.state);
    }

}
