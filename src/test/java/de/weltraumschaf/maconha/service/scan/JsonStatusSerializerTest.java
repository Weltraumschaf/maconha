package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.maconha.service.ScanService.ScanStatus;
import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

/**
 * Tests for {@link JsonStatusSerializer}.
 */
public final class JsonStatusSerializerTest {
    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    private final JsonStatusSerializer sut = new JsonStatusSerializer();

    @Test
    public void serialize_emptyCollection() throws IOException, JSONException {
        final Path statusFile = tmp.newFile().toPath();

        try (final BufferedWriter writer = Files.newBufferedWriter(statusFile)) {
            sut.serialize(Collections.emptyList(), writer);
        }

        assertEquals("[]", new String(Files.readAllBytes(statusFile)), false);
    }

    @Test
    public void serialize_oneElementCollection() throws IOException, JSONException {
        final Path statusFile = tmp.newFile().toPath();
        final Collection<ScanStatus> statuses = Collections.singletonList(new ScanStatus(
            42L,
            "bucketOne",
            "created",
            "started",
            "ended",
            "duration",
            "status",
            "type"
        ));

        try (final BufferedWriter writer = Files.newBufferedWriter(statusFile)) {
            sut.serialize(statuses, writer);
        }

        assertEquals(
            "[{\"id\":42,\"bucketName\":\"bucketOne\",\"creationTime\":\"created\",\"startTime\":\"started\",\"endTime\":\"ended\",\"duration\":\"duration\",\"jobStatus\":\"status\",\"jobExitCode\":\"status\",\"allFailureExceptions\":[],\"type\":\"type\"}]",
            new String(Files.readAllBytes(statusFile)), false);
    }

    @Test
    public void serialize_threeElementsCollection() throws IOException, JSONException {
        final Path statusFile = tmp.newFile().toPath();
        final Collection<ScanStatus> statuses = Arrays.asList(
            new ScanStatus(
                42L,
                "bucketOne",
                "created",
                "started",
                "ended",
                "duration",
                "status",
                "type"
            ), new ScanStatus(
                43L,
                "bucketTwo",
                "created",
                "started",
                "ended",
                "duration",
                "status",
                "type"
            ), new ScanStatus(
                44L,
                "bucketThree",
                "created",
                "started",
                "ended",
                "duration",
                "status",
                "type"
            )
        );

        try (final BufferedWriter writer = Files.newBufferedWriter(statusFile)) {
            sut.serialize(statuses, writer);
        }

        assertEquals(
            "[{\"id\":42,\"bucketName\":\"bucketOne\",\"creationTime\":\"created\",\"startTime\":\"started\",\"endTime\":\"ended\",\"duration\":\"duration\",\"jobStatus\":\"status\",\"jobExitCode\":\"status\",\"allFailureExceptions\":[],\"type\":\"type\"}," +
                "{\"id\":43,\"bucketName\":\"bucketTwo\",\"creationTime\":\"created\",\"startTime\":\"started\",\"endTime\":\"ended\",\"duration\":\"duration\",\"jobStatus\":\"status\",\"jobExitCode\":\"status\",\"allFailureExceptions\":[],\"type\":\"type\"}," +
                "{\"id\":44,\"bucketName\":\"bucketThree\",\"creationTime\":\"created\",\"startTime\":\"started\",\"endTime\":\"ended\",\"duration\":\"duration\",\"jobStatus\":\"status\",\"jobExitCode\":\"status\",\"allFailureExceptions\":[],\"type\":\"type\"}]",
            new String(Files.readAllBytes(statusFile)), false);
    }

    @Test
    public void deserialize_empty() {
        final Collection<ScanStatus> result = sut.deserialize(new StringReader(""));

        assertThat(result, is(not(nullValue())));
        assertThat(result, is(empty()));
    }

    @Test
    public void deserialize_zeroElements() {
        final Collection<ScanStatus> result = sut.deserialize(new StringReader("[]"));

        assertThat(result, is(not(nullValue())));
        assertThat(result, is(empty()));
    }

    @Test
    public void deserialize_oneElement() {
        final Collection<ScanStatus> result = sut.deserialize(new StringReader(
            "[{\"id\":42,\"bucketName\":\"bucketOne\",\"creationTime\":\"created\",\"startTime\":\"started\",\"endTime\":\"ended\",\"duration\":\"duration\",\"jobStatus\":\"status\",\"jobExitCode\":\"status\",\"allFailureExceptions\":[],\"type\":\"type\"}]"));

        assertThat(result, containsInAnyOrder(
            new ScanStatus(
                42L,
                "bucketOne",
                "created",
                "started",
                "ended",
                "duration",
                "status",
                "type"
            )
        ));
    }

    @Test
    public void deserialize_threeElements() {
        final Collection<ScanStatus> result = sut.deserialize(new StringReader(
            "[{\"id\":42,\"bucketName\":\"bucketOne\",\"creationTime\":\"created\",\"startTime\":\"started\",\"endTime\":\"ended\",\"duration\":\"duration\",\"jobStatus\":\"status\",\"jobExitCode\":\"status\",\"allFailureExceptions\":[],\"type\":\"type\"}," +
                "{\"id\":43,\"bucketName\":\"bucketTwo\",\"creationTime\":\"created\",\"startTime\":\"started\",\"endTime\":\"ended\",\"duration\":\"duration\",\"jobStatus\":\"status\",\"jobExitCode\":\"status\",\"allFailureExceptions\":[],\"type\":\"type\"}," +
                "{\"id\":44,\"bucketName\":\"bucketThree\",\"creationTime\":\"created\",\"startTime\":\"started\",\"endTime\":\"ended\",\"duration\":\"duration\",\"jobStatus\":\"status\",\"jobExitCode\":\"status\",\"allFailureExceptions\":[],\"type\":\"type\"}]"));

        assertThat(result, containsInAnyOrder(
            new ScanStatus(
                42L,
                "bucketOne",
                "created",
                "started",
                "ended",
                "duration",
                "status",
                "type"
            ), new ScanStatus(
                43L,
                "bucketTwo",
                "created",
                "started",
                "ended",
                "duration",
                "status",
                "type"
            ), new ScanStatus(
                44L,
                "bucketThree",
                "created",
                "started",
                "ended",
                "duration",
                "status",
                "type"
            )
        ));
    }
}
