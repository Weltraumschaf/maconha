package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.model.FileMetaData;
import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;

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
}
