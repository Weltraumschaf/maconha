package de.weltraumschaf.maconha.dao;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.model.Keyword;
import org.hibernate.criterion.Restrictions;
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

    @Override
    public Keyword findByLiteral(final String literal) {
        Validate.notNull(literal, "literal");
        return (Keyword) createEntityCriteria()
            .add(Restrictions.eq("literal", literal))
            .uniqueResult();
    }

}
