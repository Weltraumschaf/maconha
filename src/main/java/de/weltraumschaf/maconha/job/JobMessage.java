package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import java.util.Objects;

/**
 */
public final class JobMessage {

    private final String producerName;
    private final String content;

    public JobMessage(final String producerName, final String content) {
        super();
        this.producerName = Validate.notEmpty(producerName, "producerName");
        this.content = Validate.notEmpty(content, "content");
    }

    public String getProducerName() {
        return producerName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int hashCode() {
        return Objects.hash(producerName, content);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JobMessage)) {
            return false;
        }

        final JobMessage other = (JobMessage) obj;
        return Objects.equals(this.producerName, other.producerName)
            && Objects.equals(this.content, other.content);
    }

    @Override
    public String toString() {
        return "JobMessage{" + "producerName=" + producerName + ", content=" + content + '}';
    }

}
