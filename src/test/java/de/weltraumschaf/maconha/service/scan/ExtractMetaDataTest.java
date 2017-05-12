package de.weltraumschaf.maconha.service.scan;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 */
public final class ExtractMetaDataTest {

    @Test
    public void test() throws TikaException, IOException {
        final Tika tika = new Tika();
        final Metadata metadata = new Metadata();
        final Path file = Paths.get("/Users/sst/Music/iTunes/iTunes Media/Music/Pink Floyd/A Foot In the Door_ The Best of Pink Floyd/1-01 Hey You.m4a");

        try (final TikaInputStream input = TikaInputStream.get(file)) {
            final MediaType mimetype = tika.getDetector().detect(input, metadata);
            System.out.println("File " + file + " is " + mimetype);
            System.out.println(tika.parseToString(input));
        }
    }
}
