package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.job.Job.State;
import java.util.Objects;

/**
 * Describes a {@link Job}.
 */
public final class JobDescription {

    private final String name;
    private final Class<? extends Job> type;
    private final State state;

    /**
     * Dedicated constructor.
     *
     * @param name must not be {@code null} or empty
     * @param type must not be {@code null}
     * @param state must not be {@code null}
     */
    public JobDescription(final String name, final Class<? extends Job> type, final State state) {
        super();
        this.name = Validate.notEmpty(name, "name");
        this.type = Validate.notNull(type, "type");
        this.state = Validate.notNull(state, "state");
    }

    public String getName() {
        return name;
    }

    public Class<? extends Job> getType() {
        return type;
    }

    public State getState() {
        return state;
    }

    @Override
    public String toString() {
        return "name=" + name + ", type=" + type + ", state=" + state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, state);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JobDescription)) {
            return false;
        }

        final JobDescription other = (JobDescription) obj;
        return Objects.equals(name, other.name)
            && Objects.equals(type, other.type)
            && Objects.equals(state, other.state);
    }


}
