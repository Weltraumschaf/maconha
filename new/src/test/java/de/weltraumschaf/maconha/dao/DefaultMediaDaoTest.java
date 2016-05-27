
package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.maconha.MaconhaApplication;
import java.io.InputStream;
import javax.sql.DataSource;
import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DefaultMediaDao}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MaconhaApplication.class)
public class DefaultMediaDaoTest extends DataSourceBasedDBTestCase {

    @Autowired
    private MediaDao sut;
    @Autowired
    private DataSource db;

    @Before
    public void verifyAutowiredObjects() {
        assertThat("Subject under test must not be null!", sut, is(not(nullValue())));
        assertThat("Data source must not be null!", db, is(not(nullValue())));
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        final InputStream dataset = getClass().getResourceAsStream("/dataset.xml");
        assertThat("Dataset file must not be null!", dataset, is(not(nullValue())));
        return new FlatXmlDataSetBuilder().build(dataset);
    }

    @Override
    protected DataSource getDataSource() {
        return db;
    }

    @Test
    public void testSomeMethod() {
        sut.findAll();
    }



}
