package de.weltraumschaf.maconha.model;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Entity which represent an origin scanned file.
 * <p>
 * Must not be {@code final} for frameworks sake.
 * </p>
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
public class OriginFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 1, max = 255)
    private String baseDir;

    @NotNull
    @Size(min = 1, max = 255)
    private String absolutePath;

    /**
     * SHA-256 (e.g. "2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae")
     */
    @NotNull
    @Size(min = 64, max = 64)
    private String fingerprint;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime indexTime;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public Path getBaseDir() {
        return Paths.get(baseDir);
    }

    public void setBaseDir(Path baseDir) {
        this.baseDir = baseDir.toString();
    }

    public Path getAbsolutePath() {
        return Paths.get(absolutePath);
    }

    public void setAbsolutePath(Path absolutePath) {
        this.absolutePath = absolutePath.toString();
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public LocalDateTime getIndexTime() {
        return indexTime;
    }

    public void setIndexTime(LocalDateTime indexTime) {
        this.indexTime = indexTime;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, baseDir, absolutePath, fingerprint, indexTime);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (!(obj instanceof OriginFile)) {
            return false;
        }

        final OriginFile other = (OriginFile) obj;

        return Objects.equals(id, other.id)
            && Objects.equals(fingerprint, other.fingerprint)
            && Objects.equals(baseDir, other.baseDir)
            && Objects.equals(absolutePath, other.absolutePath)
            && Objects.equals(indexTime, other.indexTime);
    }

    @Override
    public String toString() {
        return "OriginFile{" +
            "id=" + id + ", "
            + "baseDir=" + baseDir + ", "
            + "absolutePath=" + absolutePath + ", "
            + "fingerprint=" + fingerprint + ", "
            + "indexTime=" + indexTime + '}';
    }

}
