package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.core.FileExtension;
import de.weltraumschaf.maconha.job.ProgressMonitor;
import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.model.OriginFile;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.repo.MediaRepo;
import de.weltraumschaf.maconha.repo.OriginFileRepo;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.LocalDateTime;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link DefaultMediaService};
 */
//@Transactional
//@ActiveProfiles("test")
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(MaconhaApplication.class)
public class DefaultMediaServiceTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();
    private final KeywordRepo keywordRepo = mock(KeywordRepo.class);
    private final MediaRepo mediaRepo = mock(MediaRepo.class);
    private final OriginFileRepo originFileRepo = mock(OriginFileRepo.class);
    private final DefaultMediaService sut = new DefaultMediaService(keywordRepo, mediaRepo, originFileRepo);

    @Test
    @Ignore
    public void generateIndex() throws Exception {
        final Media mediaOne = new Media(); // No file, will be ignored.
        final Media mediaTwo = new Media().setOriginFile(
            new OriginFile()
            .setBaseDir(Paths.get("/foo/bar"))
            .setAbsolutePath(Paths.get("/foo/bar/Movie_Two.avi"))
        );
        final Media mediaThree = new Media().setOriginFile(
            new OriginFile()
            .setBaseDir(Paths.get("/foo/bar"))
            .setAbsolutePath(Paths.get("/foo/bar/Movie_Three.avi"))
        );
        when(mediaRepo.findAll()).thenReturn(Arrays.asList(mediaOne, mediaTwo, mediaThree));
        final Keyword movieKeyword = new Keyword().setId(42).setLiteral("movie");
        when(keywordRepo.findByLiteral("movie")).thenReturn(movieKeyword);

        sut.generateIndex(new ProgressMonitor());

        verify(keywordRepo, times(1)).save(new Keyword().setLiteral("two").addMedias(mediaTwo));
        verify(keywordRepo, times(1)).save(new Keyword().setLiteral("three").addMedias(mediaThree));
        verify(mediaRepo, never()).save(mediaOne);
    }

    @Test
    @Ignore
    public void importMedia() throws Exception {
        final OriginFile fileOne = new OriginFile()
            .setId(1)
            .setBaseDir(Paths.get("/foo/movies"))
            .setAbsolutePath(Paths.get("/foo/movies/file_one.avi"))
            .setFingerprint(DigestUtils.sha256Hex("fileOne"))
            .setScanTime(new LocalDateTime());
        final OriginFile fileTwo = new OriginFile()
            .setId(2)
            .setBaseDir(Paths.get("/foo/movies"))
            .setAbsolutePath(Paths.get("/foo/movies/file_two.mov"))
            .setFingerprint(DigestUtils.sha256Hex("fileTwo"))
            .setScanTime(new LocalDateTime())
            .setImported(new Media());
        final OriginFile fileThree = new OriginFile()
            .setId(3)
            .setBaseDir(Paths.get("/foo/movies"))
            .setAbsolutePath(Paths.get("/foo/movies/file_three.mpg"))
            .setFingerprint(DigestUtils.sha256Hex("fileThree"))
            .setScanTime(new LocalDateTime());
        when(originFileRepo.findAll()).thenReturn(Arrays.asList(fileOne, fileTwo, fileThree));

        sut.importMedia(new ProgressMonitor());

        verify(mediaRepo, times(1)).save(new Media()
            .setTitle("file one")
            .setType(Media.MediaType.VIDEO)
            .setFormat(FileExtension.AUDIO_VIDEO_INTERLEAVE)
//            .setLastImported(sut.getImportTime())
            .setOriginFile(fileOne));
        verify(mediaRepo, times(1)).save(new Media()
            .setTitle("file three")
            .setType(Media.MediaType.VIDEO)
            .setFormat(FileExtension.MPEG_VIDEO_FILE)
//            .setLastImported(sut.getImportTime())
            .setOriginFile(fileThree));
    }


    @Test
    @Ignore
    public void execute() throws Exception {
        final URI fixtures = getClass().getResource("/de/weltraumschaf/maconha/job/ScanJob").toURI();
        final Path baseDir = Paths.get(fixtures);

        sut.scanDirecotry(new ProgressMonitor(), baseDir);

//        final Collection<OriginFile> persisted = db.findAll();
//        assertThat(persisted, hasSize(13));
//        assertThat(resetIds(persisted), containsInAnyOrder(originFiles(baseDir).toArray()));
    }

    private Collection<OriginFile> resetIds(final Collection<OriginFile> files) {
        files.stream().forEach(file -> file.setId(0));
        return files;
    }

    private Collection<OriginFile> originFiles(final Path baseDir) {
        final Map<String, String> pathAndHash = new HashMap<>();
        pathAndHash.put("Animation/adolf-bunker.wmv", "1c9470f7f9489acb2b93398d79d989ea2f6e2f45625448d4cef168c2aff83d94");
        pathAndHash.put("Animation/android_207_HQ.mp4", "2f04335a0b0a96b50f9e19de9fa5a4aac2c0cc42e474bf3b9401790e33e166cb");
        pathAndHash.put("Animation/animusic/Animusic-AcousticCurves.wmv", "37055372aa9ab1679aeab43d9534fe65d0b217c84679b1ff43274e2b4a58a308");
        pathAndHash.put("Animation/animusic/Animusic-AquaHarp.wmv", "3a77f9b6e3aa5fed770f94b3aadcd284c8d1f8dc0ced62a018ab6671ab73c8f9");
        pathAndHash.put("Animation/Big_Buck_Bunny_1080p_h264.mov", "97808a192766278937d24da7277c0a859c80986897603aa1c78634a7f7fbaa1b");
        pathAndHash.put("Animation/bird.swf", "560709de5b9cffa3977ecd6577f2ffb6aab663d6e96ae1a2e9ca7afea833c479");
        pathAndHash.put("Animation/Elephants_Dream_1024-h264-st-aac.mov", "0e8f4f0f3c68f3a14ac73550e3553f4c24f1a811d1fc388bd2b92bc44034370b");
        pathAndHash.put("Animation/Elephants_Dream_1024.avi", "487370c3a7c086142b8a67873b2cbccec8d66c4bde918843aa9550d206bcf2f7");
        pathAndHash.put("Comedy/Ali-G-Show/da.ali.g.show.s02e01.avi", "2275ed5740ca60be7e26e98b4b03dbc65adb3c33278f1d3f59cdabb0e7ce4b3c");
        pathAndHash.put("Comedy/MAD TV - Arnold's Musical (Birgi).mpg", "ff7cb6281f58ba9806622c99334c6f118dd2dcad17b064d76e3c000da30d4602");
        pathAndHash.put("Comedy/Mystery_Sience_Theater_3000.avi", "38dd96733f6c31fbfb139fbdf71497d783ddf88d8d995e0f20a7ffc815c234ac");
        pathAndHash.put("Comedy/Volker_Pispers/3sat-Festival_2001.mp4", "b9956847b822abc010689aeb9b4564aaee7437d064c66c96c2021d77ed5edea1");
        pathAndHash.put("Comedy/Volker_Pispers/bis_neulich_2007.mp4", "bdec11ab87443f2182a71635b405e7a04e38324dd0b3092469be56862228ca93");

        return pathAndHash.entrySet().stream().map((e) -> {
            final OriginFile file = originFile(baseDir);
            file.setAbsolutePath(baseDir.resolve(e.getKey()));
            file.setFingerprint(e.getValue());
            return file;
        }).collect(Collectors.toList());
    }

    private OriginFile originFile(final Path baseDir) {
        final OriginFile file = new OriginFile();
        file.setBaseDir(baseDir);
        file.setScanTime(sut.getStartTime());
        return file;
    }
}
