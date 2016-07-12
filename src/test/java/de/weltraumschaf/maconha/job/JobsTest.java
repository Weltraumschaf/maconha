package de.weltraumschaf.maconha.job;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * Tests for {@link Jobs}.
 */
public class JobsTest {

    private final AutowireCapableBeanFactory beanFactory = mock(AutowireCapableBeanFactory.class);
    private final Jobs sut = new Jobs(beanFactory);

    @Test(expected = NullPointerException.class)
    public void create_null() {
        sut.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_empty() {
        sut.create("");
    }

    @Test
    public void findImplementations() {
        assertThat(sut.findImplementations(), containsInAnyOrder(
            ScanDirectoryJob.class,
            HashFilesJob.class,
            ImportMediaJob.class,
            GenerateIndexJob.class,
            NoOpJob.class
        ));
    }

    @Test
    public void generateLookUp() {
        assertThat(sut.generateLookUp(sut.findImplementations()), allOf(
            hasEntry(ScanDirectoryJob.DESCRIPTION.name(), ScanDirectoryJob.DESCRIPTION),
            hasEntry(HashFilesJob.DESCRIPTION.name(), HashFilesJob.DESCRIPTION),
            hasEntry(ImportMediaJob.DESCRIPTION.name(), ImportMediaJob.DESCRIPTION),
            hasEntry(GenerateIndexJob.DESCRIPTION.name(), GenerateIndexJob.DESCRIPTION),
            hasEntry(NoOpJob.DESCRIPTION.name(), NoOpJob.DESCRIPTION)
        ));
    }
}
