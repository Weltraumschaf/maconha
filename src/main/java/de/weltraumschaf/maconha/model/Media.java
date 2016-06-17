package de.weltraumschaf.maconha.model;

import de.weltraumschaf.maconha.core.FileExtension;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * Entity which represent imported media.
 * <p>
 * Must not be {@code final} for frameworks sake.
 * </p>
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
public class Media extends BaseEntity {

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
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String title = "";

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(iso = ISO.DATE_TIME)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime lastImported = new LocalDateTime();

    // Fixme Optional should be false.
    @OneToOne(optional = true, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "originFile_id", referencedColumnName = "id", insertable = true, updatable = true)
    private OriginFile originFile;

    @ManyToMany(mappedBy = "medias")
    @SuppressWarnings("FieldMayBeFinal")
    private Set<Keyword> keywords = new HashSet<>();

    public int getId() {
        return id;
    }

    public Media setId(final int id) {
        this.id = id;
        return this;
    }

    public MediaType getType() {
        return type;
    }

    public Media setType(final MediaType type) {
        this.type = type;
        return this;
    }

    public FileExtension getFormat() {
        return format;
    }

    public Media setFormat(final FileExtension format) {
        this.format = format;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Media setTitle(final String title) {
        this.title = title;
        return this;
    }

    public LocalDateTime getLastImported() {
        return lastImported;
    }

    public Media setLastImported(final LocalDateTime lastImported) {
        this.lastImported = lastImported;
        return this;
    }

    public OriginFile getOriginFile() {
        return originFile;
    }

    public Media setOriginFile(final OriginFile originFile) {
        if (sameAsFormer(originFile)) {
            return this;
        }

        this.originFile = originFile;
        this.originFile.setImported(this);
        return this;
    }

    private boolean sameAsFormer(final OriginFile newOriginFile) {
        return sameAsFormer(originFile, newOriginFile);
    }

    public Set<Keyword> getKeywords() {
        return new HashSet<>(keywords);
    }

    public Media addKeyword(final Keyword keyword) {
        if (isAlreadyAdded(keyword)) {
            return this;
        }

        keywords.add(keyword);
        keyword.addMedias(this);
        return this;
    }

    private boolean isAlreadyAdded(final Keyword newKeyword) {
        return isAlreadyAdded(keywords, newKeyword);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, type, format, title, lastImported);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (!(obj instanceof Media)) {
            return false;
        }

        final Media other = (Media) obj;
        return Objects.equals(id, other.id)
            && Objects.equals(type, other.type)
            && Objects.equals(format, other.format)
            && Objects.equals(title, other.title)
            && Objects.equals(lastImported, other.lastImported);
    }

    @Override
    public final String toString() {
        return "Media{"
            + "id=" + id + ", "
            + "type=" + type + ", "
            + "format=" + format + ", "
            + "title=" + title + ", "
            + "lastImported=" + lastImported + ", "
            // Do not use toString() to prevent endless loop.
            + "originFile=" + (null == originFile ? "" : originFile.getAbsolutePath().toString()) + ", "
            // Do not use toString() to prevent endless loop.
            + "keywords=" + (null == keywords ? "" : keywords.stream().map(k -> k.getLiteral()).collect(Collectors.joining(", ")))
            + '}';
    }

    public static enum MediaType {

        VIDEO, AUDIO, OTHER;

        private static final Collection<FileExtension> VIDEOS = Collections.unmodifiableCollection(Arrays.asList(
            FileExtension.AUDIO_VIDEO_INTERLEAVE,
            FileExtension.DIVX_ENCODED_MOVIE_FILE,
            FileExtension.ITUNES_VIDEO_FILE,
            FileExtension.MATROSKA_VIDEO_FILE,
            FileExtension.APPLE_QUICKTIME_MOVIE,
            FileExtension.MPEG4_VIDEO_FILE,
            FileExtension.MPEG_MOVIE,
            FileExtension.MPEG_VIDEO_FILE,
            FileExtension.OGG_MEDIA_FILE,
            FileExtension.REAL_MEDIA_FILE,
            FileExtension.SHOCKWAVE_FLASH_MOVIE,
            FileExtension.WINDOWS_MEDIA_VIDEO_FILE,
            FileExtension.XVID_ENCODED_VIDEO_FILE
        ));
        private static final Collection<FileExtension> AUDIOS = Collections.unmodifiableCollection(Arrays.asList());

        private static final Map<MediaType, Collection<FileExtension>> LOOKUP;

        static {
            Map<MediaType, Collection<FileExtension>> tmp = new HashMap<>();
            tmp.put(VIDEO, VIDEOS);
            tmp.put(AUDIO, AUDIOS);
            LOOKUP = Collections.unmodifiableMap(tmp);
        }

        public static MediaType forValue(final FileExtension extension) {
            if (null == extension) {
                return OTHER;
            }

            for (final Entry<MediaType, Collection<FileExtension>> pair : LOOKUP.entrySet()) {
                if (pair.getValue().contains(extension)) {
                    return pair.getKey();
                }
            }

            return OTHER;
        }
    };
}
