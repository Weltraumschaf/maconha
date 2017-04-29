package de.weltraumschaf.maconha.frontend.admin.view.buckets;

import de.weltraumschaf.maconha.model.Bucket;

import java.io.Serializable;

/**
 * Event to notify that a bucket was notified.
 */
final class BucketModifiedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    BucketModifiedEvent() {
        super();
    }
}
