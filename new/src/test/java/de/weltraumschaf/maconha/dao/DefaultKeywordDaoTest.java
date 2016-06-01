
package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.maconha.MaconhaApplication;
import javax.transaction.Transactional;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for {@link DefaultKeywordDao}.
 */
@Transactional
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MaconhaApplication.class)
public class DefaultKeywordDaoTest {

    @Autowired
    private KeywordDao sut;

    @Before
    public void verifyAutowiredObjects() {
        assertThat("Subject under test must not be null!", sut, is(not(nullValue())));
    }

    @Test
    @Ignore("TODO Implement test.")
    public void save() {}

    @Test
    @Ignore("TODO Implement test.")
    public void delete() {}

    @Test
    @Ignore("TODO Implement test.")
    public void deleteById() {}

    @Test
    @Ignore("TODO Implement test.")
    public void findAll() {}

}
