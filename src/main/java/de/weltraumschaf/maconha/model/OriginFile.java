package de.weltraumschaf.maconha.model;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
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
    @Column(unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @Column(nullable = false)
    @Size(min = 1, max = 255)
    private String baseDir;

    @NotEmpty
    @Column(nullable = false)
    @Size(min = 1, max = 255)
    private String absolutePath;

    /**
     * SHA-256 (e.g. "2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae")
     */
    @NotEmpty
    @Column(nullable = false)
    @Size(min = 64, max = 64)
    private String fingerprint;

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime scanTime = new LocalDateTime();

    @OneToOne(optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "id", referencedColumnName = "originFile_id", insertable = true, updatable = true)
    private Media imported;

    public int getId() {
        return id;
    }

    public OriginFile setId(final int id) {
        this.id = id;
        return this;
    }

    public Path getBaseDir() {
        return Paths.get(baseDir);
    }

    public OriginFile setBaseDir(Path baseDir) {
        this.baseDir = baseDir.toString();
        return this;
    }

    public Path getAbsolutePath() {
        return Paths.get(absolutePath);
    }

    public OriginFile setAbsolutePath(Path absolutePath) {
        this.absolutePath = absolutePath.toString();
        return this;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public OriginFile setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
        return this;
    }

    public LocalDateTime getIndexTime() {
        return scanTime;
    }

    public OriginFile setScanTime(LocalDateTime scanTime) {
        this.scanTime = scanTime;
        return this;
    }

    public Media getImported() {
        return imported;
    }

    public OriginFile setImported(Media imported) {
        this.imported = imported;
        return this;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, baseDir, absolutePath, fingerprint, scanTime, imported);
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
            && Objects.equals(scanTime, other.scanTime)
            && Objects.equals(imported, other.imported);
    }

    @Override
    public String toString() {
        return "OriginFile{" +
            "id=" + id + ", "
            + "baseDir=" + baseDir + ", "
            + "absolutePath=" + absolutePath + ", "
            + "fingerprint=" + fingerprint + ", "
            + "scanTime=" + scanTime + ", "
            + "imported=" + imported
            + '}';
    }

}