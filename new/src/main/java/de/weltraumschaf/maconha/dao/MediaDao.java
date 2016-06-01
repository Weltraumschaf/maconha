package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.maconha.model.Media;
import java.util.Collection;

/**
 * Data layer abstraction for {@link Media}.
 */
public interface MediaDao {

    Media findById(int id);

    Collection<Media> findAll();

    void save(Media entity);

    void delete(Media entity);

    void deleteById(int id);

}
