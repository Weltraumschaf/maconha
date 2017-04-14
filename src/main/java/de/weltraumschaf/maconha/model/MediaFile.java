package de.weltraumschaf.maconha.model;

import de.weltraumschaf.maconha.core.FileExtension;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Entity which represent imported media.
 * <p>
 * Must not be {@code final} for frameworks sake.
 * </p>
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
public class MediaFile extends BaseEntity {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType type = MediaType.OTHER;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileExtension format = FileExtension.NONE;

    @NotEmpty
    @Size(min = 1, max = 4096)
    @Column(nullable = false)
    private String relativeFileName = "";

    @NotEmpty
    @Size(min = 1, max = 256)
    @Column(nullable = false)
    private String fileHash = "";

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(iso = ISO.DATE_TIME)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime lastScanned = new LocalDateTime();

    @ManyToMany(mappedBy = "mediaFiles")
    @SuppressWarnings("FieldMayBeFinal")
    private Set<Keyword> keywords = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(final MediaType type) {
        this.type = type;
    }

    public FileExtension getFormat() {
        return format;
    }

    public void setFormat(final FileExtension format) {
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

    private boolean isAlreadyAdded(final Keyword newKeyword) {
        return isAlreadyAdded(keywords, newKeyword);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, type, format, relativeFileName, fileHash, lastScanned);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (!(obj instanceof MediaFile)) {
            return false;
        }

        final MediaFile other = (MediaFile) obj;
        return Objects.equals(id, other.id)
            && Objects.equals(type, other.type)
            && Objects.equals(format, other.format)
            && Objects.equals(relativeFileName, other.relativeFileName)
            && Objects.equals(fileHash, other.fileHash)
            && Objects.equals(lastScanned, other.lastScanned);
    }

    @Override
    public final String toString() {
        return "MediaFile{"
            + "id=" + id + ", "
            + "type=" + type + ", "
            + "format=" + format + ", "
            + "relativeFileName=" + relativeFileName + ", "
            + "fileHash=" + fileHash + ", "
            + "lastScanned=" + lastScanned + ", "
            // Do not use toString() to prevent endless loop.
            + "keywords=" + (null == keywords ? "" : keywords.stream().map(k -> k.getLiteral()).collect(Collectors.joining(", ")))
            + '}';
    }
}
