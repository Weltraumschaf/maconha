package de.weltraumschaf.maconha.backend.repo;

import de.weltraumschaf.maconha.backend.model.FileExtension;
import de.weltraumschaf.maconha.backend.model.entity.MediaFile;
import de.weltraumschaf.maconha.backend.model.entity.MediaFile_;
import de.weltraumschaf.maconha.backend.model.MediaType;
import de.weltraumschaf.maconha.backend.repo.MediaFileRepo.MediaFileSpecifications;
import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link MediaFileSpecifications}.
 */
public final class MediaFileSpecificationsTest {

    @Test
    public void getContainsLikePattern_termIsNull() {
        assertThat(MediaFileSpecifications.createContainsLikePattern(null), is("%"));
    }

    @Test
    public void getContainsLikePattern_isEmpty() {
        assertThat(MediaFileSpecifications.createContainsLikePattern(""), is("%"));
    }

    @Test
    public void getContainsLikePattern_isBlank() {
        assertThat(MediaFileSpecifications.createContainsLikePattern("    "), is("%"));
    }

    @Test
    public void getContainsLikePattern() {
        assertThat(MediaFileSpecifications.createContainsLikePattern("foo"), is("%foo%"));
    }

    @Test
    public void getContainsLikePattern_searchTermIsTrimmed() {
        assertThat(MediaFileSpecifications.createContainsLikePattern("  foo  "), is("%foo%"));
    }

    @Test
    public void getContainsLikePattern_searchTermIsLowerCased() {
        assertThat(MediaFileSpecifications.createContainsLikePattern("FOO"), is("%foo%"));
    }

    @Test
    @Ignore
    @SuppressWarnings("unchecked")
    public void relativeFileNameIgnoreCaseAndTypeAndFormat_relativeFileNameIsNull() {
        final Specification<MediaFile> specification = MediaFileSpecifications.relativeFileNameIgnoreCaseAndTypeAndFormat(
            null,
            MediaType.AUDIO,
            FileExtension.WINDOWS_AUDIO_VIDEO);

        assertThat(specification, is(not(nullValue())));

        final CriteriaBuilderImpl cb = new CriteriaBuilderImpl(mock(EntityManagerFactoryImpl.class));
        Root root = mock(Root.class);
        when(root.get(MediaFile_.relativeFileName)).thenReturn(mock(Path.class));
        when(root.get(MediaFile_.type)).thenReturn(mock(Path.class));
        when(root.get(MediaFile_.format)).thenReturn(mock(Path.class));

        final Predicate predicate = specification.toPredicate(
            root,
            mock(CriteriaQuery.class),
            cb);

        assertThat(predicate, is(not(nullValue())));
    }

    @Test
    @Ignore
    public void relativeFileNameIgnoreCaseAndTypeAndFormat_relativeFileNameIsEmpty() {
        final Specification<MediaFile> specification = MediaFileSpecifications.relativeFileNameIgnoreCaseAndTypeAndFormat(
            "",
            MediaType.AUDIO,
            FileExtension.WINDOWS_AUDIO_VIDEO);
    }

    @Test
    @Ignore
    public void relativeFileNameIgnoreCaseAndTypeAndFormat_relativeFileNameIsBlank() {
        final Specification<MediaFile> specification = MediaFileSpecifications.relativeFileNameIgnoreCaseAndTypeAndFormat(
            "   ",
            MediaType.AUDIO,
            FileExtension.WINDOWS_AUDIO_VIDEO);
    }

    @Test
    @Ignore
    public void relativeFileNameIgnoreCaseAndTypeAndFormat_typeIsNull() {
        final Specification<MediaFile> specification = MediaFileSpecifications.relativeFileNameIgnoreCaseAndTypeAndFormat(
            "foo",
            null,
            FileExtension.WINDOWS_AUDIO_VIDEO);
    }

    @Test
    @Ignore
    public void relativeFileNameIgnoreCaseAndTypeAndFormat_formatIsNull() {
        final Specification<MediaFile> specification = MediaFileSpecifications.relativeFileNameIgnoreCaseAndTypeAndFormat(
            "foo",
            MediaType.AUDIO,
            null);
    }

    @Test
    @Ignore
    public void relativeFileNameIgnoreCaseAndTypeAndFormat() {
        final Specification<MediaFile> specification = MediaFileSpecifications.relativeFileNameIgnoreCaseAndTypeAndFormat(
            "foo",
            MediaType.AUDIO,
            FileExtension.WINDOWS_AUDIO_VIDEO);

        assertThat(specification, is(not(nullValue())));
    }
}
