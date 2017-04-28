package de.weltraumschaf.maconha.service.search;

import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link DefaultSearchService}.
 */
public final class DefaultSearchServiceTest {
    private final KeywordRepo keywords = mock(KeywordRepo.class);
    private MediaFileRepo mediaFiles = mock(MediaFileRepo.class);
    private final DefaultSearchService sut = new DefaultSearchService(keywords, mediaFiles);

    @Test(expected = NullPointerException.class)
    public void forKeywords_nullGiven() {
        sut.forKeywords(null);
    }

    @Test
    public void forKeywords_emptyGiven() {
        assertThat(sut.forKeywords(Collections.emptyList()), is(empty()));
    }

    @Test
    @Ignore
    public void forKeywords() {
        final Keyword foo = new Keyword();
        foo.setLiteral("foo");
        final Keyword bar = new Keyword();
        bar.setLiteral("bar");
        final Keyword baz = new Keyword();
        baz.setLiteral("baz");

        final MediaFile file1 = new MediaFile();
        file1.setRelativeFileName("file1");
        file1.setFileHash("2f04335a0b0a96b50f9e19de9fa5a4aac2c0cc42e474bf3b9401790e33e166cb");
        file1.addKeyword(foo);
        file1.addKeyword(bar);
        file1.addKeyword(baz);

        final MediaFile file2 = new MediaFile();
        file2.setRelativeFileName("file2");
        file2.setFileHash("37055372aa9ab1679aeab43d9534fe65d0b217c84679b1ff43274e2b4a58a308");
        file2.addKeyword(foo);
        file2.addKeyword(bar);

        final MediaFile file3 = new MediaFile();
        file3.setRelativeFileName("file3");
        file3.setFileHash("3a77f9b6e3aa5fed770f94b3aadcd284c8d1f8dc0ced62a018ab6671ab73c8f9");
        file3.addKeyword(foo);
        file3.addKeyword(baz);

        final MediaFile file4 = new MediaFile();
        file4.setRelativeFileName("file4");
        file4.setFileHash("1c9470f7f9489acb2b93398d79d989ea2f6e2f45625448d4cef168c2aff83d94");
        file4.addKeyword(foo);

        final MediaFile file5 = new MediaFile();
        file5.setRelativeFileName("file5");
        file5.setFileHash("560709de5b9cffa3977ecd6577f2ffb6aab663d6e96ae1a2e9ca7afea833c479");
        file5.addKeyword(bar);

        final List<String> query = Arrays.asList(foo.getLiteral(), bar.getLiteral(), baz.getLiteral());
        when(keywords.findByLiteralIn(query)).thenReturn(Arrays.asList(foo, bar, baz));

        assertThat(
            sut.forKeywords(query),
            is(Arrays.asList(file1, file2, file3, file4, file5)));
    }
}
