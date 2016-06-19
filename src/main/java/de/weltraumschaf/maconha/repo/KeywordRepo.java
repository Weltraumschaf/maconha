package de.weltraumschaf.maconha.repo;

import de.weltraumschaf.maconha.model.Keyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Auto implemented by Spring.
 */
public interface KeywordRepo extends BaseRepo<Keyword>{

    Keyword findByLiteral(String literal);
    @Override
    Page<Keyword> findAll(Pageable pageable);
}
