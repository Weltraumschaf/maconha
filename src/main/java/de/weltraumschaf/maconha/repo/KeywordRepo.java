package de.weltraumschaf.maconha.repo;

import de.weltraumschaf.maconha.model.Keyword;

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
