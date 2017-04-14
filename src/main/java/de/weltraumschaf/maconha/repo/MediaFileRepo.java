package de.weltraumschaf.maconha.repo;

import de.weltraumschaf.maconha.model.MediaFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Auto implemented by Spring.
 */
public interface MediaFileRepo extends BaseRepo<MediaFile> {

    @Override
    Page<MediaFile> findAll(Pageable pageable);
}
