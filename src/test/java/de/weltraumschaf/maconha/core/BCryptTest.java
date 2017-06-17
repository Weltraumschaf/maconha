package de.weltraumschaf.maconha.core;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link BCrypt}.
 */
public final class BCryptTest {
    private final BCrypt sut = new BCrypt();

    @Test(expected = IllegalArgumentException.class)
    public void encodeBase64_numberOfBytesToEncodeIsLessThanZero() {
        sut.encodeBase64(new byte[0], -1, new StringBuilder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodeBase64_numberOfBytesToEncodeIsZero() {
        sut.encodeBase64(new byte[0], 0, new StringBuilder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodeBase64_numberOfBytesToEncodeIsGreaterThanToEncodeLength() {
        sut.encodeBase64(new byte[10], 11, new StringBuilder());
    }

    @Test
    public void encodeBase64() {
        final StringBuilder buffer = new StringBuilder();

        sut.encodeBase64(
            new byte[] {0x41, 0x42, 0x43, 0x44, 0x45},
            5,
            buffer);

        assertThat(buffer.toString(), is("OSHBPCS"));
    }
}
