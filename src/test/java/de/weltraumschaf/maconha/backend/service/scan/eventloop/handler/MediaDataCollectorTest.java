package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.backend.model.FileMetaData;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link MediaDataCollector}.
 */
public final class MediaDataCollectorTest {
    private final HashedFile hashedFile = new HashedFile("hash", "file");
    private final MediaDataCollector sut = new MediaDataCollector(hashedFile);

    @Test
    public void equalsAndHAshCode() {
        EqualsVerifier.forClass(MediaDataCollector.class).verify();
    }

    @Test(expected = NullPointerException.class)
    public void hashedFileMustNotBeNull() {
        new MediaDataCollector(null);
    }

    @Test
    public void getFile() {
        assertThat(sut.getFile(), is(hashedFile));
    }

    @Test
    public void setMetaData() {
        final FileMetaData metaData = new FileMetaData("mime", "data");
        assertThat(sut.getMetaData(), is(FileMetaData.NOTHING));

        final MediaDataCollector newCollector = sut.setMetaData(metaData);

        assertThat(sut.getMetaData(), is(FileMetaData.NOTHING));
        assertThat(sut, is(not(newCollector)));
        assertThat(newCollector.getFile(), is(sut.getFile()));
        assertThat(newCollector.getKeywords(), is(sut.getKeywords()));
        assertThat(newCollector.getMetaData(), is(metaData));
    }

    @Test
    public void addKeyWords() {
        assertThat(sut.getKeywords(), hasSize(0));

        final MediaDataCollector newCollector = sut.addKeyWords(Arrays.asList("k1", "k2"));

        assertThat(sut.getKeywords(), hasSize(0));
        assertThat(sut, is(not(newCollector)));
        assertThat(newCollector.getFile(), is(sut.getFile()));
        assertThat(newCollector.getMetaData(), is(sut.getMetaData()));
        assertThat(newCollector.getKeywords(), contains("k1", "k2"));
    }

    @Test
    public void addKeyWords_addMoreKeywords() {
        final MediaDataCollector newCollector = sut.addKeyWords(Arrays.asList("k1", "k2"))
            .addKeyWords(Arrays.asList("k3", "k4"));

        assertThat(newCollector.getKeywords(), contains("k1", "k2", "k3", "k4"));
    }

    @Test
    public void addKeyWords_ignoresDuplicates() {
        final MediaDataCollector newCollector = sut.addKeyWords(Arrays.asList("k1", "k2"))
            .addKeyWords(Arrays.asList("k1", "k2", "k3", "k4"));

        assertThat(newCollector.getKeywords(), contains("k1", "k2", "k3", "k4"));
    }
}
