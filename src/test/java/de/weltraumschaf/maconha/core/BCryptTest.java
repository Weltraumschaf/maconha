package de.weltraumschaf.maconha.core;

import org.junit.Test;

import java.security.SecureRandom;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test(expected = IllegalArgumentException.class)
    public void decodeBase64_numberOfBytesToEncodeLessThanZero() {
        sut.decodeBase64("", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decodeBase64_numberOfBytesToEncodeIsZero() {
        sut.decodeBase64("", 0);
    }

    @Test
    public void decodeBase64() {
        assertThat(
            sut.decodeBase64("OSHBPCS", 5),
            is(new byte[] {0x41, 0x42, 0x43, 0x44, 0x45}));
    }

    @Test
    public void hashPassword() {
        assertThat(
            sut.hashPassword("foobar", "$2a$10$..CA.uOD/eaGAOmJB.yMBu"),
            is("$2a$10$..CA.uOD/eaGAOmJB.yMBuEItK539BIYsNIBpajujHnYivL.phUEW"));
    }

    @Test
    public void generateSalt() {
        final SecureRandom random = mock(SecureRandom.class);
        doAnswer(invocation -> {
            byte[] rnd = invocation.getArgument(0);

            for (byte i = 0; i < BCrypt.BCRYPT_SALT_LEN; ++i) {
                rnd[i] = i;
            }

            return null;
        }).when(random).nextBytes(new byte[BCrypt.BCRYPT_SALT_LEN]);

        assertThat(sut.generateSalt(10, random), is("$2a$10$..CA.uOD/eaGAOmJB.yMBu"));
    }

    @Test
    public void checkPassword() {
        assertThat(
            sut.checkPassword(
                "foobar",
                "$2a$10$..CA.uOD/eaGAOmJB.yMBuEItK539BIYsNIBpajujHnYivL.phUEW"),
            is(true));
        assertThat(
            sut.checkPassword(
                "snafu",
                "$2a$10$..CA.uOD/eaGAOmJB.yMBuEItK539BIYsNIBpajujHnYivL.phUEW"),
            is(false));
    }
}
