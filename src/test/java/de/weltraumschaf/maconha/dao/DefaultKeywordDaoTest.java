package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.maconha.MaconhaApplication;
import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.Media;
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
 * Tests for {@link DefaultKeywordDao}.
 */
@Transactional
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MaconhaApplication.class)
public class DefaultKeywordDaoTest {

    @Autowired
    private KeywordDao sut;
    @Autowired
    private MediaDao dao;

    @Before
    public void verifyAutowiredObjects() {
        assertThat("Subject under test must not be null!", sut, is(not(nullValue())));
    }

    @Test
    public void save() {
        final Keyword toSave = new Keyword();
        toSave.setLiteral("keyword");
        assertThat(toSave.getId(), is(0));

        sut.save(toSave);

        assertThat(toSave.getId(), is(greaterThan(0)));
        final Keyword loaded = sut.findById(toSave.getId());
        assertThat(loaded, is(toSave));
    }

    @Test
    public void delete() {
        final Keyword toDelete = new Keyword();
        toDelete.setLiteral("keyword");
        sut.save(toDelete);
        final int id = toDelete.getId();

        sut.delete(toDelete);

        assertThat(sut.findById(id), is(nullValue()));
    }

    @Test
    public void deleteById() {
        final Keyword toDelete = new Keyword();
        toDelete.setLiteral("keyword");
        sut.save(toDelete);
        final int id = toDelete.getId();

        sut.deleteById(id);

        assertThat(sut.findById(id), is(nullValue()));
    }

    @Test
    public void findAll() {
        final Keyword one = new Keyword();
        one.setLiteral("one");
        sut.save(one);
        final Keyword two = new Keyword();
        two.setLiteral("two");
        sut.save(two);
        final Keyword three = new Keyword();
        three.setLiteral("three");
        sut.save(three);

        assertThat(sut.findAll(), hasSize(3));
        assertThat(sut.findAll(), containsInAnyOrder(one, two, three));
    }

    @Test
    public void save_withMedias() {
        final Media mediaOne = new Media()
            .setTitle("media one");
        final Media mediaTwo = new Media()
            .setTitle("media two");
        final Media mediaThree = new Media()
            .setTitle("media three");
        final Keyword keywordOne = new Keyword()
            .setLiteral("one")
            .addMedias(mediaOne)
            .addMedias(mediaTwo);
        final Keyword keywordTwo = new Keyword()
            .setLiteral("two")
            .addMedias(mediaTwo)
            .addMedias(mediaThree);

        sut.save(keywordOne);
        sut.save(keywordTwo);

        final Keyword loadedOne = sut.findById(keywordOne.getId());
        assertThat(loadedOne.getMedias(), containsInAnyOrder(mediaOne, mediaTwo));
        final Keyword loadedTwo = sut.findById(keywordTwo.getId());
        assertThat(loadedTwo.getMedias(), containsInAnyOrder(mediaTwo, mediaThree));
    }

}
