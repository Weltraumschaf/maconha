package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.job.Job.State;

/**
 */
public final class Description {
    private final String name;
    private final Class<? extends Job> type;
    private final State state;

    public Description(final String name, final Class<? extends Job> type, final State state) {
        super();
        this.name = name;
        this.type = type;
        this.state = state;
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

}
