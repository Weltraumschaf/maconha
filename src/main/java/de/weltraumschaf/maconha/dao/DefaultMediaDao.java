package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.maconha.model.Media;
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

}
