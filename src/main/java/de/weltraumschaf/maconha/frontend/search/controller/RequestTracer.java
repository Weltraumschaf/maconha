package de.weltraumschaf.maconha.frontend.search.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Helper facility to trace incincomingquests.
 */
final class RequestTracer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestTracer.class);

    void traceRequest(final HttpServletRequest request) {
        LOGGER.debug("URI {} requested.", getRequestUrl(request));
    }

    private String getRequestUrl(final HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

}
