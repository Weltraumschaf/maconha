package de.weltraumschaf.maconha.frontend.admin.view.buckets;

import de.weltraumschaf.maconha.model.Bucket;

import java.io.Serializable;

/**
 *
 */
final class BucketModifiedEvent implements Serializable {
    private final Bucket bucket;

    public BucketModifiedEvent(final Bucket bucket) {
        super();
        this.bucket = bucket;
    }

    public Bucket getBucket() {
        return bucket;
    }
}
