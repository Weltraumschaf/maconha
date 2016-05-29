package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.maconha.model.Media;
import java.util.Collection;
import org.hibernate.Session;

/**
 */
public interface MediaDao {

    Media findById(int id);

    Collection<Media> findAll();

    void save(Media media);

    void delete(Media media);

    void deleteById(int id);

    Session getSession();
}
