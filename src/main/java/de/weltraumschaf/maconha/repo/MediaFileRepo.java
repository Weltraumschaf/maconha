package de.weltraumschaf.maconha.repo;

import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.model.MediaFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Auto implemented by Spring.
 */
public interface MediaFileRepo extends BaseRepo<MediaFile> {

    @Override
    Page<MediaFile> findAll(Pageable pageable);

    MediaFile findByRelativeFileName(String relativeFileName);

    MediaFile findByRelativeFileNameAndBucket(String relativeFileName, Bucket bucket);

    List<MediaFile> findByRelativeFileNameLikeIgnoreCase(String likeFilter);
}
