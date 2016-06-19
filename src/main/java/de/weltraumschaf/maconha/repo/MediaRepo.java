package de.weltraumschaf.maconha.repo;

import de.weltraumschaf.maconha.model.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Auto implemented by Spring.
 */
public interface MediaRepo extends BaseRepo<Media> {

    @Override
    Page<Media> findAll(Pageable pageable);
}
