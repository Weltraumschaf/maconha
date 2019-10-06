package de.weltraumschaf.maconha.ui.controller;

import de.weltraumschaf.maconha.app.Application;
import de.weltraumschaf.maconha.backend.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;

/**
 * Tests for {@link IndexController}.
 *
 * https://spring.io/guides/gs/testing-web/
 */
@WebMvcTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
public final class IndexControllerTest {
    @MockBean
    private UserService users;
    @Autowired
    private MockMvc mvc;

    @Test
    public void index_redirectToInstallIfNoAdminUserExists() {

    }

    @TestConfiguration
    class UserServiceTestConfiguration {
        @Bean
        public UserService eventFacade() {
            return users;
        }
    }
}
