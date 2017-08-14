package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.model.FileMetaData;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 *
 */
final class MediaDataCollector {
    private final HashedFile file;
    private final Collection<String> keywords = new HashSet<>();
    private FileMetaData metaData = FileMetaData.NOTHING;

    MediaDataCollector(final HashedFile file) {
        super();
        this.file = Validate.notNull(file, "file");
    }

    HashedFile getFile() {
        return file;
    }

    FileMetaData getMetaData() {
        return metaData;
    }

    MediaDataCollector addMetaData(final FileMetaData metaData) {
        final MediaDataCollector newCollector = copy();
        newCollector.metaData = metaData;
        return newCollector;
    }

    Collection<String> getKeywords() {
        return Collections.unmodifiableCollection(keywords);
    }

    MediaDataCollector addKeyWords(final Collection<String> keywords) {
        final MediaDataCollector newCollector = copy();
        newCollector.keywords.addAll(keywords);
        return newCollector;
    }

    private MediaDataCollector copy() {
        final MediaDataCollector newCollector = new MediaDataCollector(file);
        newCollector.keywords.addAll(keywords);
        newCollector.metaData = metaData;
        return newCollector;
    }
}
