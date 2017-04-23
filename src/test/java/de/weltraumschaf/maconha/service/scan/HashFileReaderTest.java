package de.weltraumschaf.maconha.service.scan;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Set;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link HashFileReader}.
 */
public class HashFileReaderTest {

    private final HashFileReader sut = new HashFileReader();

    @Test(expected = NullPointerException.class)
    public void read_pathMustNotBeNull() throws IOException {
        sut.read(null);
    }

    @Test
    public void read() throws IOException, URISyntaxException {
        final URI fixture = getClass().getResource("/de/weltraumschaf/maconha/service/scan/checksums").toURI();

        final Set<HashedFile> result = sut.read(Paths.get(fixture));

        assertThat(result, containsInAnyOrder(
            new HashedFile(
                "2f04335a0b0a96b50f9e19de9fa5a4aac2c0cc42e474bf3b9401790e33e166cb",
                "foo/Animation/android_207_HQ.mp4"),
            new HashedFile(
                "37055372aa9ab1679aeab43d9534fe65d0b217c84679b1ff43274e2b4a58a308",
                "foo/Animation/animusic/Animusic-AcousticCurves.wmv"),
            new HashedFile(
                "3a77f9b6e3aa5fed770f94b3aadcd284c8d1f8dc0ced62a018ab6671ab73c8f9",
                "foo/Animation/animusic/Animusic-AquaHarp.wmv"),
            new HashedFile(
                "1c9470f7f9489acb2b93398d79d989ea2f6e2f45625448d4cef168c2aff83d94",
                "foo/Animation/adolf-bunker.wmv"),
            new HashedFile(
                "560709de5b9cffa3977ecd6577f2ffb6aab663d6e96ae1a2e9ca7afea833c479",
                "foo/Animation/bird.swf"),
            new HashedFile(
                "0e8f4f0f3c68f3a14ac73550e3553f4c24f1a811d1fc388bd2b92bc44034370b",
                "foo/Animation/Elephants_Dream_1024-h264-st-aac.mov"),
            new HashedFile(
                "97808a192766278937d24da7277c0a859c80986897603aa1c78634a7f7fbaa1b",
                "foo/Animation/Big_Buck_Bunny_1080p_h264.mov"),
            new HashedFile(
                "487370c3a7c086142b8a67873b2cbccec8d66c4bde918843aa9550d206bcf2f7",
                "foo/Animation/Elephants_Dream_1024.avi"),
            new HashedFile(
                "ff7cb6281f58ba9806622c99334c6f118dd2dcad17b064d76e3c000da30d4602",
                "foo/Comedy/MAD TV - Arnold's Musical (Birgi).mpg"),
            new HashedFile(
                "2275ed5740ca60be7e26e98b4b03dbc65adb3c33278f1d3f59cdabb0e7ce4b3c",
                "foo/Comedy/Ali-G-Show/da.ali.g.show.s02e01.avi"),
            new HashedFile(
                "38dd96733f6c31fbfb139fbdf71497d783ddf88d8d995e0f20a7ffc815c234ac",
                "foo/Comedy/Mystery_Sience_Theater_3000.avi"),
            new HashedFile(
                "b9956847b822abc010689aeb9b4564aaee7437d064c66c96c2021d77ed5edea1",
                "foo/Comedy/Volker_Pispers/3sat-Festival_2001.mp4"),
            new HashedFile(
                "bdec11ab87443f2182a71635b405e7a04e38324dd0b3092469be56862228ca93",
                "foo/Comedy/Volker_Pispers/bis_neulich_2007.mp4")
        ));
    }
}
