package de.weltraumschaf.maconha;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.job.JobExecutor;
import java.util.Arrays;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main entry point of the Spring boot application.
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan({"de.weltraumschaf.maconha"})
@PropertySource(value = {"classpath:application.properties"})
public class MaconhaApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaconhaApplication.class);

    @Autowired
    private Environment environment;

    /**
     * Invoked main method.
     *
     * @param args arguments from command line
     */
    public static void main(final String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(MaconhaApplication.class, args);
        LOGGER.trace("Provided beans:");

        final String[] beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames);

        for (final String beanName : beanNames) {
            LOGGER.trace(beanName);
        }
    }

    @Bean
    public JobExecutor jobExecutor() {
        LOGGER.debug("Create new job executor.");
        return new JobExecutor();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public SessionFactory sessionFactory() {
        LOGGER.debug("Create session factory bean.");
        return new LocalSessionFactoryBuilder(dataSource())
            .scanPackages(new String[]{"de.weltraumschaf.maconha.model"})
            .addProperties(hibernateProperties())
            .buildSessionFactory();
    }

    @Bean
    public DataSource dataSource() {
        LOGGER.debug("Create data source bean.");
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));

        return dataSource;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(final SessionFactory sessions) {
        LOGGER.debug("Create transaction manager bean with session factory {}.", sessions);
        final HibernateTransactionManager manager = new HibernateTransactionManager();
        manager.setSessionFactory(Validate.notNull(sessions, "sessions"));
        return manager;
    }

    private Properties hibernateProperties() {
        final Properties properties = new Properties();

        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));

        return properties;
    }
}
