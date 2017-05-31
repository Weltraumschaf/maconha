package de.weltraumschaf.maconha.service.scan;

import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.model.Bucket;

import java.util.Objects;

/**
 * Data structure to remember started scan jobs.
 */
final class Execution {
    private Long id;
    private final Bucket bucket;
    private UI currentUi;

    Execution(final Long id, final Bucket bucket, final UI currentUi) {
        super();
        this.id = id;
        this.bucket = bucket;
        this.currentUi = currentUi;
    }

    Long getId() {
        return id;
    }

    Bucket getBucket() {
        return bucket;
    }

    UI getCurrentUi() {
        return currentUi;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Execution)) {
            return false;
        }

        final Execution execution = (Execution) o;
        return Objects.equals(id, execution.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Execution{" +
            "id=" + id +
            ", bucket=" + bucket +
            ", currentUi=" + currentUi +
            '}';
    }
}
