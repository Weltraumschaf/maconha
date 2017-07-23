package de.weltraumschaf.maconha.app;

//import com.vaadin.spring.access.SecuredViewAccessControl;
//import org.springframework.context.annotation.Bean;
import de.weltraumschaf.maconha.backend.service.ScanServiceFactory;
import de.weltraumschaf.maconha.backend.service.scan.batch.ScanBatchConfiguration;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * General application configuration.
 */
@Configuration
public class ApplicationConfiguration {

    /**
     * The password encoder to use when encrypting passwords.
     *
     * @return never {@code null}, always new instance
     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    /**
     * Special implementation to use Spring Security in conjunction with Vaadin.
     *
     * @return never {@code null}, always new instance
     */
//    @Bean
//    public SecuredViewAccessControl securedViewAccessControl() {
//        return new SecuredViewAccessControl();
//    }

    @Bean
    public ApplicationContextFactory scanJobFactory() {
        // Provide a context factory for the scan job so the job registry will be populated with the job.
        return new GenericApplicationContextFactory(ScanBatchConfiguration.class);
    }

    @Bean
    public FactoryBean serviceLocatorFactoryBean() {
        final ServiceLocatorFactoryBean bean = new ServiceLocatorFactoryBean();
        bean.setServiceLocatorInterface(ScanServiceFactory.class);
        return bean;
    }
}
