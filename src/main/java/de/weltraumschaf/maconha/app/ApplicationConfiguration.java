package de.weltraumschaf.maconha.app;

import com.vaadin.spring.access.SecuredViewAccessControl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * General application configuration.
 */
@Configuration
public class ApplicationConfiguration {

    private static final int JOB_LIMIT = 10;

    /**
     * The password encoder to use when encrypting passwords.
     *
     * @return never {@code null}, always new instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Special implementation to use Spring Security in conjunction with Vaadin.
     *
     * @return never {@code null}, always new instance
     */
    @Bean
    public SecuredViewAccessControl securedViewAccessControl() {
        return new SecuredViewAccessControl();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        final SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("scan_job");
        taskExecutor.setConcurrencyLimit(JOB_LIMIT);
        return taskExecutor;
    }
}
