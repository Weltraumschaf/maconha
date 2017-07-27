package de.weltraumschaf.maconha.ui.controller;

import org.springframework.http.MediaType;

/**
 * The default content type this application serves.
 */
interface ServedContentTypes {
    /**
     * The default served.
     */
    String TEXT_HTML = MediaType.TEXT_HTML_VALUE;
    /**
     * The default served for REST responses.
     */
    String APPLICATION_JSON = MediaType.APPLICATION_JSON_VALUE;
}
