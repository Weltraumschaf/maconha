package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.maconha.MaconhaApplication;
import de.weltraumschaf.maconha.model.OriginFile;
import java.nio.file.Paths;
import javax.transaction.Transactional;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for {@link DefaultOriginFileDao}.
 */
@Transactional
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MaconhaApplication.class)
public class DefaultOriginFileDaoTest {

    @Autowired
    private OriginFileDao sut;

    @Before
    public void verifyAutowiredObjects() {
        assertThat("Subject under test must not be null!", sut, is(not(nullValue())));
    }

    @Test
    public void save() {
        final OriginFile toSave = new OriginFile();
        toSave.setBaseDir(Paths.get("/base/dir"));
        toSave.setAbsolutePath(Paths.get("/absolute/path"));
        toSave.setFingerprint("2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae");
        assertThat(toSave.getId(), is(0));

        sut.save(toSave);

        assertThat(toSave.getId(), is(greaterThan(0)));
        final OriginFile loaded = sut.findById(toSave.getId());
        assertThat(loaded, is(toSave));
    }

    @Test
    public void delete() {
        final OriginFile toDelete = new OriginFile();
        toDelete.setBaseDir(Paths.get("/base/dir"));
        toDelete.setAbsolutePath(Paths.get("/absolute/path"));
        toDelete.setFingerprint("2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae");
        sut.save(toDelete);
        final int id = toDelete.getId();

        sut.delete(toDelete);

        assertThat(sut.findById(id), is(nullValue()));
    }

    @Test
    public void deleteById() {
        final OriginFile toDelete = new OriginFile();
        toDelete.setBaseDir(Paths.get("/base/dir"));
        toDelete.setAbsolutePath(Paths.get("/absolute/path"));
        toDelete.setFingerprint("2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae");
        sut.save(toDelete);
        final int id = toDelete.getId();

        sut.deleteById(id);

        assertThat(sut.findById(id), is(nullValue()));
    }

    @Test
    public void findAll() {
        final OriginFile one = new OriginFile();
        one.setBaseDir(Paths.get("/base/dir/one"));
        one.setAbsolutePath(Paths.get("/absolute/path/one"));
        one.setFingerprint("2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae");
        sut.save(one);
        final OriginFile two = new OriginFile();
        two.setBaseDir(Paths.get("/base/dir/two"));
        two.setAbsolutePath(Paths.get("/absolute/path/two"));
        two.setFingerprint("2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae");
        sut.save(two);
        final OriginFile three = new OriginFile();
        three.setBaseDir(Paths.get("/base/dir/three"));
        three.setAbsolutePath(Paths.get("/absolute/path/three"));
        three.setFingerprint("2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae");
        sut.save(three);

        assertThat(sut.findAll(), hasSize(3));
        assertThat(sut.findAll(), containsInAnyOrder(one, two, three));
    }
}
