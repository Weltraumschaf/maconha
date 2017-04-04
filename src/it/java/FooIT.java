import de.weltraumschaf.maconha.MaconhaApplication;
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
@ContextConfiguration(classes = {MaconhaApplication.class})
public class FooIT {

//    @Autowired
//    private JobService jobs;

    @Test
    @Ignore
    public void foo() {
//        assertThat(jobs, is(not(nullValue())));
    }
}
