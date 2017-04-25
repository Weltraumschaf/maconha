package de.weltraumschaf.maconha.repo;

import de.weltraumschaf.maconha.model.Keyword;

import java.util.Collection;

/**
 * Auto implemented by Spring.
 */
public interface KeywordRepo extends BaseRepo<Keyword> {

    Keyword findByLiteral(String literal);

    Collection<Keyword> findByLiteralIn(Collection<String> literals);
}
