package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.maconha.model.OriginFile;
import java.util.Collection;

/**
 * Data layer abstraction for {@link OriginFile}.
 */
public interface OriginFileDao {

    OriginFile findById(int id);

    Collection<OriginFile> findAll();

    void save(OriginFile entity);

    void delete(OriginFile entity);

    void deleteById(int id);
}
