package de.weltraumschaf.maconha.repos;

import de.weltraumschaf.maconha.model.Keyword;

/**
 * Auto implemented by Spring.
 */
public interface KeywordRepo extends BaseRepo<Keyword>{

    Keyword findByLiteral(String literal);

}
