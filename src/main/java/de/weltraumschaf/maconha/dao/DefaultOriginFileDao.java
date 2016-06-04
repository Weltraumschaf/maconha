package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.maconha.model.OriginFile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Must not be final for Spring.
 */
@Transactional
@Repository("originFileDao")
public class DefaultOriginFileDao extends BaseDao<OriginFile> implements OriginFileDao {

    public DefaultOriginFileDao() {
        super(OriginFile.class);
    }

}
