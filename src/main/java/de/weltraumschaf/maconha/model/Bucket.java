package de.weltraumschaf.maconha.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Entity which represent a bucket: A directory location which contains media files.
 * <p>
 * Must not be {@code final} for frameworks sake.
 * </p>
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
public class Bucket extends BaseEntity{
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @Size(min = 1, max = 4096)
    @Column(unique = true, nullable = false)
    private String directory;

    @NotEmpty
    @Size(min = 1, max = 512)
    @Column(unique = false, nullable = false)
    private String name;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(final String directory) {
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Bucket)) {
            return false;
        }

        final Bucket other = (Bucket) o;
        return id == other.id &&
            Objects.equals(directory, other.directory) &&
            Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, directory, name);
    }

    @Override
    public String toString() {
        return "Bucket{" +
            "id=" + id +
            ", directory='" + directory + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
