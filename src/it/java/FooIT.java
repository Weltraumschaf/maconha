import de.weltraumschaf.maconha.app.Application;
import de.weltraumschaf.maconha.backend.service.SearchService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Example integration test case.
 */
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
public class FooIT {

    @Autowired
    private SearchService search;

    @Test
    @Ignore
    public void foo() {
        assertThat(search, is(not(nullValue())));
    }
}
