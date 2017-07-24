package de.weltraumschaf.maconha.backend.repo;

import de.weltraumschaf.maconha.backend.model.entity.Keyword;

import java.util.Collection;
import java.util.List;

/**
 * Auto implemented by Spring.
 */
public interface KeywordRepo extends BaseRepo<Keyword> {

    Keyword findByLiteral(String literal);
    List<Keyword> findByLiteralLike(String literal);

    Collection<Keyword> findByLiteralIn(Collection<String> literals);

}
