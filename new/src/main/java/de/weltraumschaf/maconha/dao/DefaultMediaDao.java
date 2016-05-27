package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.maconha.model.Media;
import java.util.Collection;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Must not be final for Spring.
 */
@Transactional
@Repository("mediaDao")
public class DefaultMediaDao extends BaseDao<Media> implements MediaDao {

    public DefaultMediaDao() {
        super(Media.class);
    }

    @Override
    public void deleteById(final int id) {
        final Query query = getSession().createSQLQuery("delete from Media where id = :id");
        query.setInteger("id", id);
        query.executeUpdate();
    }

    @Override
    public Collection<Media> findAll() {
        return createEntityCriteria().list();
    }

}
