package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.maconha.model.Keyword;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Must not be final for Spring.
 */
@Transactional
@Repository("keywordDao")
public class DefaultKeywordDao extends BaseDao<Keyword> implements KeywordDao {

    public DefaultKeywordDao() {
        super(Keyword.class);
    }

}
