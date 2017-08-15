package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.service.mediafile.IgnoredKeywords;
import de.weltraumschaf.maconha.backend.service.mediafile.MalformedKeywords;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.Event;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventContext;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventHandler;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public final class FilterUnwantedKeywords extends BaseHandler implements EventHandler, HasLogger {
    private final Collection<Predicate<String>> filters;

    public FilterUnwantedKeywords() {
        this(Arrays.asList(new MalformedKeywords(), new IgnoredKeywords()));
    }

    FilterUnwantedKeywords(final Collection<Predicate<String>> filters) {
        super();
        this.filters = new ArrayList<>(filters);
    }

    @Override
    public void process(final EventContext context, final Event event) {
        assertPreConditions(context, event, MediaDataCollector.class);

        final MediaDataCollector collector = (MediaDataCollector) event.getData();
        logger().debug("Filter unwanted keywords ({}) of file {} ...", collector.getKeywords(), collector.getFile());
        Stream<String> keywords = collector.getKeywords().stream();

        for (final Predicate<String> filter : filters) {
            keywords = keywords.filter(filter);
        }

        context.emitter()
            .emmit(new Event(
                EventType.STORE_FILE_AND_KEYWORDS,
                collector.setKeyWords(keywords.collect(Collectors.toList()))));
    }
}
