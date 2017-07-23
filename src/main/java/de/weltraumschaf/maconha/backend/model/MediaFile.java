package de.weltraumschaf.maconha.backend.model;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entity which represent imported media.
 * <p>
 * Must not be {@code final} for frameworks sake.
 * </p>
 */
@Entity
@Table(indexes = {
    @Index(name = "idx_type", columnList = "type"),
    @Index(name = "idx_fileHash", columnList = "fileHash")
})
public class MediaFile extends BaseEntity {

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType type = MediaType.OTHER;

    @NotEmpty
    @Column(nullable = false)
    private String format = "";

    @NotEmpty
    @Size(min = 1, max = 4096)
    @Column(nullable = false)
    private String relativeFileName = "";

    @NotEmpty
    @Size(min = 64, max = 64)
    @Column(nullable = false)
    private String fileHash = "";

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(iso = ISO.DATE_TIME)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime lastScanned = new LocalDateTime();

    @ManyToMany(mappedBy = "mediaFiles", cascade = CascadeType.ALL)
    @SuppressWarnings("FieldMayBeFinal")
    private Set<Keyword> keywords = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;

    public MediaType getType() {
        return type;
    }

    public void setType(final MediaType type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(final String format) {
        this.format = format;
    }

    public String getRelativeFileName() {
        return relativeFileName;
    }

    public void setRelativeFileName(final String relativeFileName) {
        this.relativeFileName = relativeFileName;
    }

    public LocalDateTime getLastScanned() {
        return lastScanned;
    }

    public void setLastScanned(final LocalDateTime lastScanned) {
        this.lastScanned = lastScanned;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(final String fileHash) {
        this.fileHash = fileHash;
    }

    public Set<Keyword> getKeywords() {
        return new HashSet<>(keywords);
    }

    public void addKeyword(final Keyword keyword) {
        if (isAlreadyAdded(keyword)) {
            return;
        }

        keywords.add(keyword);
        keyword.addMedias(this);
    }

    public Bucket getBucket() {
        return bucket;
    }

    public void setBucket(final Bucket bucket) {
        this.bucket = bucket;
    }

    private boolean isAlreadyAdded(final Keyword newKeyword) {
        return isAlreadyAdded(keywords, newKeyword);
    }

    @Override
    public final int hashCode() {
        // Id is not considered because it is different for new objects and persisted ones.
        // Keywords not considered because this would lead to endless recursion.
        return Objects.hash(type, format, relativeFileName, fileHash, lastScanned, bucket);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (!(obj instanceof MediaFile)) {
            return false;
        }

        final MediaFile other = (MediaFile) obj;
        // Id is not considered because it is different for new objects and persisted ones.
        // Keywords not considered because this would lead to endless recursion.
        return Objects.equals(type, other.type)
            && Objects.equals(format, other.format)
            && Objects.equals(relativeFileName, other.relativeFileName)
            && Objects.equals(fileHash, other.fileHash)
            && Objects.equals(lastScanned, other.lastScanned)
            && Objects.equals(bucket, other.bucket);
    }

    @Override
    public final String toString() {
        return "MediaFile{"
            + "id=" + getId() + ", "
            + "type=" + type + ", "
            + "format=" + format + ", "
            + "relativeFileName=" + relativeFileName + ", "
            + "fileHash=" + fileHash + ", "
            + "lastScanned=" + lastScanned + ", "
            // Do not use toString() to prevent endless loop.
            + "keywords=" + keywords.stream().map(Keyword::getLiteral).collect(Collectors.joining(", ")) + ", "
            + "bucket=" + bucket
            + '}';
    }
}
