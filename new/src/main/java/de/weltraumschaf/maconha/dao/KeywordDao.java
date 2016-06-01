package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.maconha.model.Keyword;
import java.util.Collection;

/**
 * Data layer abstraction for {@link Keyword}.
 */
public interface KeywordDao {

    Keyword findById(int id);

    Collection<Keyword> findAll();

    void save(Keyword entity);

    void delete(Keyword entity);

    void deleteById(int id);
}
