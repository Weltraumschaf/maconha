package de.weltraumschaf.maconha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * This is a manual database configuration because auto config only via properties does notwork w/ current SpringBoot version.
 */
@Configuration
@EnableJpaRepositories(basePackages = { "de.weltraumschaf.maconha.repo" })
public class DatabaseConfiguration {
    @Value("${spring.datasource.driverClassName}")
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

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("de.iteratec.str.iteratweet.model");

        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    private Properties additionalProperties() {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.naming_strategy", "org.hibernate.cfg.EJB3NamingStrategy");
        return properties;
    }
}
