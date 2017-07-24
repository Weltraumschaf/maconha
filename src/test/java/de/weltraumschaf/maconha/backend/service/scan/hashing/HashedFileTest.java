
package de.weltraumschaf.maconha.backend.service.scan.hashing;

import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.model.FileExtension;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link HashedFile}.
 */
public class HashedFileTest {

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_hashMustNotBeNull() {
        new HashedFile(null, "file");
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_hashMustNotBeEmpty() {
        new HashedFile("", "file");
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_fileMustNotBeNull() {
        new HashedFile("hash", null);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_fileMustNotBeEmpty() {
        new HashedFile("hash", "");
    }

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(HashedFile.class).verify();
    }

    @Test
    public void extractExtension() {
        assertThat(
            new HashedFile("hash", "/foo/bar/snafu.avi").extractExtension(),
            is(FileExtension.AUDIO_VIDEO_INTERLEAVE));
    }

    @Test
    public void relativizeFilename_withoutTrailingSlash() {
        final Bucket bucket = new Bucket();
        bucket.setDirectory("/foo/bar");

        final HashedFile result = new HashedFile("hash", "/foo/bar/baz/snafu.avi").relativizeFilename(bucket);

        assertThat(result.getHash(), is("hash"));
        assertThat(result.getFile(), is("baz/snafu.avi"));
    }

    @Test
    public void relativizeFilename_withTrailingSlash() {
        final Bucket bucket = new Bucket();
        bucket.setDirectory("/foo/bar/");

        final HashedFile result = new HashedFile("hash", "/foo/bar/baz/snafu.avi").relativizeFilename(bucket);

        assertThat(result.getHash(), is("hash"));
        assertThat(result.getFile(), is("baz/snafu.avi"));
    }
}
