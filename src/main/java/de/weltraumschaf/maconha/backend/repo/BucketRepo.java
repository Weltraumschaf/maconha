package de.weltraumschaf.maconha.backend.repo;

import de.weltraumschaf.maconha.backend.model.Bucket;

import java.util.List;

/**
 * Auto implemented by Spring.
 */
public interface BucketRepo extends BaseRepo<Bucket> {
    List<Bucket> findByDirectoryLikeIgnoreCase(String likeFilter);

    Bucket findById(Long aLong);
}
