package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import java.util.Objects;

/**
 */
public final class JobMessage {

    private final String producerName;
    private final String body;

    public JobMessage(final String producerName, final String body) {
        super();
        this.producerName = Validate.notEmpty(producerName, "producerName");
        this.body = Validate.notEmpty(body, "body");
    }

    public String getProducerName() {
        return producerName;
    }

    public String getBody() {
        return body;
    }

    @Override
    public int hashCode() {
        return Objects.hash(producerName, body);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JobMessage)) {
            return false;
        }

        final JobMessage other = (JobMessage) obj;
        return Objects.equals(this.producerName, other.producerName)
            && Objects.equals(this.body, other.body);
    }

    @Override
    public String toString() {
        return "JobMessage{" + "producerName=" + producerName + ", body=" + body + '}';
    }

}
