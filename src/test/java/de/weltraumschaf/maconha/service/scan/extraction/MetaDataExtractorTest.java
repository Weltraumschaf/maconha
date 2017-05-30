package de.weltraumschaf.maconha.service.scan.extraction;

import de.weltraumschaf.maconha.model.FileMetaData;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link MetaDataExtractor}.
 */
public final class MetaDataExtractorTest {

    private final MetaDataExtractor sut = new MetaDataExtractor();

    private String findPath(final String basename) throws URISyntaxException {
        final URL resource = getClass()
            .getResource("/de/weltraumschaf/maconha/service/scan/extraction/" + basename);
        final Path file = Paths.get(resource.toURI());

        return file.toString();
    }

    @Test
    public void extract_mp3() throws Exception {
        final FileMetaData result = sut.extract(findPath("Drums.mp3"));

        assertThat(result.getMime(), is("audio/mpeg"));
        assertThat(
            result.getData(),
            is("Das Weltraumschaf\n" +
                "null\n" +
                "282089.9\n" +
                "eng - iTunNORM\n" +
                " 000001C4 00000000 000007F6 00000000 0001D4EE 00000000 000041EC 00000000 00000000 00000000�\n"));
    }

    @Test
    public void extract_ogg() throws Exception {
        final FileMetaData result = sut.extract(findPath("Drums.ogg"));

        assertThat(result.getMime(), is("audio/vorbis"));
        assertThat(
            result.getData(),
            is("Drums\nDas Weltraumschaf\n00:04:42.04\n"));
    }
}
