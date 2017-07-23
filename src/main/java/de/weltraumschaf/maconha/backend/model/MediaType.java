package de.weltraumschaf.maconha.backend.model;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Type of media file.
 */
public enum MediaType {
    TEXT,
    VIDEO,
    AUDIO,
    IMAGE,
    APPLICATION,
    OTHER;

    private static final Collection<FileExtension> TEXTS = collectByMimeGroup("text/");
    private static final Collection<FileExtension> VIDEOS = collectByMimeGroup("video/");
    private static final Collection<FileExtension> AUDIOS = collectByMimeGroup("audio/");
    private static final Collection<FileExtension> IMAGES = collectByMimeGroup("image/");
    private static final Collection<FileExtension> APPLICATIONS = collectByMimeGroup("application/");

    private static final Map<MediaType, Collection<FileExtension>> FILE_EXTENSION_LOOKUP;

    static {
        final Map<MediaType, Collection<FileExtension>> tmp = new HashMap<>();
        tmp.put(TEXT, TEXTS);
        tmp.put(VIDEO, VIDEOS);
        tmp.put(AUDIO, AUDIOS);
        tmp.put(IMAGE, IMAGES);
        tmp.put(APPLICATION, APPLICATIONS);
        FILE_EXTENSION_LOOKUP = Collections.unmodifiableMap(tmp);
    }

    private static final Map<String, MediaType> MEDIA_TYPE_LOOKUP;

    static {
        MEDIA_TYPE_LOOKUP = Collections.unmodifiableMap(
            Arrays.stream(values())
                .collect(Collectors.toMap(m -> m.name().toUpperCase(), Function.identity()))
        );
    }

    public static MediaType forValue(final FileExtension extension) {
        if (null == extension) {
            return OTHER;
        }

        for (final Map.Entry<MediaType, Collection<FileExtension>> pair : FILE_EXTENSION_LOOKUP.entrySet()) {
            if (pair.getValue().contains(extension)) {
                return pair.getKey();
            }
        }

        return OTHER;
    }

    public static MediaType forValue(final String mediaType) {
        if (null == mediaType || mediaType.trim().isEmpty()) {
            return OTHER;
        }

        final String normalizedMediaType = mediaType.trim().toUpperCase();

        if (MEDIA_TYPE_LOOKUP.containsKey(normalizedMediaType)) {
            return MEDIA_TYPE_LOOKUP.get(normalizedMediaType);
        }

        return OTHER;
    }

    private static Collection<FileExtension> collectByMimeGroup(final String group) {
        return Collections.unmodifiableList(
            Arrays.stream(FileExtension.values())
                .filter(extension -> extension.getMimeType().startsWith(group))
                .collect(Collectors.toList()));
    }
}