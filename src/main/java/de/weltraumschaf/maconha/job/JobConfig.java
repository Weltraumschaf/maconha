package de.weltraumschaf.maconha.job;

/**
 */
public final class JobConfig {

    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "JobConfig{" + "name=" + name + '}';
    }

}
