package de.weltraumschaf.maconha.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * This is a manual database configuration because auto config only via properties does notwork w/ current SpringBoot version.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "de.weltraumschaf.maconha.repo" })
public class DatabaseConfiguration {
    @Value("${spring.datasource.driver-class-name}")
    private String driver = "";

    @Value("${spring.datasource.url}")
    private String url = "";

    @Value("${spring.datasource.username}")
    private String user = "";

    @Value("${spring.datasource.password}")
    private String password = "";

    @Value("${spring.jpa.show-sql}")
    private String showSql = "";

    @Value("${spring.jpa.hibernate.format_sql}")
    private String formatSql = "";

    @Value(value = "${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Value(value = "${spring.jpa.database-platform}")
    private String dialect;

    void setDriver(final String driver) {
        this.driver = driver;
    }

    void setUrl(final String url) {
        this.url = url;
    }

    void setUser(final String user) {
        this.user = user;
    }

    void setPassword(final String password) {
        this.password = password;
    }

    void setShowSql(final String showSql) {
        this.showSql = showSql;
    }

    void setFormatSql(final String formatSql) {
        this.formatSql = formatSql;
    }

    void setDdlAuto(final String ddlAuto) {
        this.ddlAuto = ddlAuto;
    }

    void setDialect(final String dialect) {
        this.dialect = dialect;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("de.weltraumschaf.maconha.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalEntityManagerProperties());

        return em;
    }

    @Bean
    public DataSource dataSource() {
        return dataSource(new DriverManagerDataSource());
    }

    DataSource dataSource(final DriverManagerDataSource dataSource) {
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    Properties additionalEntityManagerProperties() {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.naming_strategy", "org.hibernate.cfg.EJB3NamingStrategy");
        return properties;
    }
}
