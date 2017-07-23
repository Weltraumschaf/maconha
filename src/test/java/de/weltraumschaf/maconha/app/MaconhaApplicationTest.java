package de.weltraumschaf.maconha.app;

import de.weltraumschaf.maconha.backend.service.ScanServiceFactory;
import org.junit.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.env.Environment;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link MaconhaApplication}.
 */
public final class MaconhaApplicationTest {
    private final MaconhaApplication sut = new MaconhaApplication(mock(Environment.class), new MaconhaConfiguration());

    @Test
    public void scanJobFactory() {
        assertThat(sut.scanJobFactory(), is(not(nullValue())));
    }

    @Test
    public void serviceLocatorFactoryBean() {
        final FactoryBean actual = sut.serviceLocatorFactoryBean();

        assertThat(actual, is(not(nullValue())));

        final Class expected = ScanServiceFactory.class;
        //noinspection uncheckedR
        assertThat(actual.getObjectType(), is(equalTo(expected)));
    }
}
