package de.weltraumschaf.maconha.repo;

import de.weltraumschaf.maconha.model.OriginFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Auto implemented by Spring.
 */
public interface  OriginFileRepo extends BaseRepo<OriginFile>{

    @Override
    Page<OriginFile> findAll(Pageable pageable);
}
