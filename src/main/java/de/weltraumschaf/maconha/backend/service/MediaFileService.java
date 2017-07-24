package de.weltraumschaf.maconha.backend.service;

import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.model.FileMetaData;
import de.weltraumschaf.maconha.backend.model.entity.MediaFile;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;

/**
 * Service to deal with {@link MediaFile media files}.
 */
public interface MediaFileService {

    /**
     * Determines if a hashed file from a bucket was never seen before.
     * <p>
     * Seen means that it was already scanned and persisted and it has not changed since then.
     * </p>
     *
     * @param file   must not be {@code null}
     * @param bucket must not be {@code null}
     * @return {@code true} if never seen, else {@code false}
     */
    boolean isFileUnseen(final HashedFile file, final Bucket bucket);

    FileMetaData extractFileMetaData(final Bucket bucket, final HashedFile file);

    void extractAndStoreMetaData(final Bucket bucket, final HashedFile file);
}
