package de.weltraumschaf.maconha.repo;

import de.weltraumschaf.maconha.model.Bucket;

import java.util.List;

/**
 * Auto implemented by Spring.
 */
public interface BucketRepo extends BaseRepo<Bucket> {
    List<Bucket> findByDirectoryLikeIgnoreCase(String likeFilter);
}
