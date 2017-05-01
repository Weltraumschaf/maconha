package de.weltraumschaf.maconha.core;

import org.springframework.http.MediaType;

/**
 * The default content type this application serves.
 */
public final class ServedContentTypes {
    /**
     * The default served.
     */
    public static final String TEXT_HTML = MediaType.TEXT_HTML_VALUE;
    /**
     * The default served for REST responses.
     */
    public static final String APPLICATION_JSON = MediaType.APPLICATION_JSON_VALUE;

    private ServedContentTypes() {
        super();
    }
}
