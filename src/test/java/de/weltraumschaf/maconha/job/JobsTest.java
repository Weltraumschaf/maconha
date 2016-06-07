
package de.weltraumschaf.maconha.job;

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
}
