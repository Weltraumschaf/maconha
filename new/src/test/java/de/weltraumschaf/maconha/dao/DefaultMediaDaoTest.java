package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.maconha.MaconhaApplication;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.model.Media.MediaType;
import javax.transaction.Transactional;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ActiveProfiles;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.joda.time.LocalDateTime;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DefaultMediaDao}.
 */
@Transactional
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MaconhaApplication.class)
public class DefaultMediaDaoTest {

    @Autowired
    private MediaDao sut;

    @Before
    public void verifyAutowiredObjects() {
        assertThat("Subject under test must not be null!", sut, is(not(nullValue())));
    }

    @Test
    public void save() {
        final Media toSave = new Media();
        toSave.setFormat("avi");
        toSave.setTitle("bar");
        toSave.setType(MediaType.VIDEO);
        toSave.setLastImported(new LocalDateTime());
        assertThat(toSave.getId(), is(0));

        sut.save(toSave);

        assertThat(toSave.getId(), is(greaterThan(0)));
        final Media loaded = sut.findById(toSave.getId());
        assertThat(loaded, is(toSave));
    }

    @Test
    public void delete() {
        final Media toDelete = new Media();
        toDelete.setFormat("avi");
        toDelete.setTitle("bar");
        toDelete.setType(MediaType.VIDEO);
        toDelete.setLastImported(new LocalDateTime());
        sut.save(toDelete);
        final int id = toDelete.getId();

        sut.delete(toDelete);

        assertThat(sut.findById(id), is(nullValue()));
    }

    @Test
    public void deleteById() {
        final Media toDelete = new Media();
        toDelete.setFormat("avi");
        toDelete.setTitle("bar");
        toDelete.setType(MediaType.VIDEO);
        toDelete.setLastImported(new LocalDateTime());
        sut.save(toDelete);
        final int id = toDelete.getId();

        sut.deleteById(id);

        assertThat(sut.findById(id), is(nullValue()));
    }

    @Test
    public void findAll() {
        final Media one = new Media();
        one.setTitle("foo");
        one.setFormat("avi");
        sut.save(one);
        final Media two = new Media();
        two.setTitle("bar");
        two.setFormat("avi");
        sut.save(two);
        final Media three = new Media();
        three.setTitle("baz");
        three.setFormat("avi");
        sut.save(three);

        assertThat(sut.findAll(), hasSize(3));
        assertThat(sut.findAll(), containsInAnyOrder(one, two, three));
    }
}
