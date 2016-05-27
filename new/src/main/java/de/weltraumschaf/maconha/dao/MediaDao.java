package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.maconha.model.Media;
import java.util.Collection;

/**
 */
public interface MediaDao {

    Media findById(int id);

    void save(Media media);

    void deleteById(int id);

    Collection<Media> findAll();
}
