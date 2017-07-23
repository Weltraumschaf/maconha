package de.weltraumschaf.maconha.app;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.Properties;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link DatabaseConfiguration}.
 */
public final class DatabaseConfigurationTest {

    private final DatabaseConfiguration sut = new DatabaseConfiguration();

    @Before
    public void injectTheProperties() {
        sut.setDriver("java.lang.Object");
        sut.setUrl("url");
        sut.setUser("user");
        sut.setPassword("password");
        sut.setShowSql("showsql");
        sut.setFormatSql("formatsql");
        sut.setDdlAuto("ddlauto");
        sut.setDialect("dialect");
    }

    @Test
    public void additionalEntityManagerProperties() {
        final Properties properties = sut.additionalEntityManagerProperties();

        assertThat(properties, is(not(nullValue())));
        assertThat(properties, hasEntry("hibernate.hbm2ddl.auto", "ddlauto"));
        assertThat(properties, hasEntry("hibernate.dialect", "dialect"));
        assertThat(properties, hasEntry("hibernate.naming_strategy", "org.hibernate.cfg.EJB3NamingStrategy"));
    }

    @Test
    public void dataSource_injectProperties() {
        final DriverManagerDataSource mock = mock(DriverManagerDataSource.class);

        assertThat(sut.dataSource(mock), is(sameInstance(mock)));

        verify(mock, times(1)).setDriverClassName("java.lang.Object");
        verify(mock, times(1)).setUrl("url");
        verify(mock, times(1)).setUsername("user");
        verify(mock, times(1)).setPassword("password");
    }

    @Test
    public void dataSource() {
        assertThat(sut.dataSource(), is(instanceOf(DriverManagerDataSource.class)));
    }

    @Test
    public void entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factory = sut.entityManagerFactory();

        assertThat(factory.getDataSource(), is(not(nullValue())));
        assertThat(factory.getJpaVendorAdapter(), is(instanceOf(HibernateJpaVendorAdapter.class)));
        assertThat(factory.getJpaPropertyMap(), hasEntry("hibernate.hbm2ddl.auto", "ddlauto"));
        assertThat(factory.getJpaPropertyMap(), hasEntry("hibernate.dialect", "dialect"));
        assertThat(factory.getJpaPropertyMap(), hasEntry("hibernate.naming_strategy", "org.hibernate.cfg.EJB3NamingStrategy"));
    }
}
