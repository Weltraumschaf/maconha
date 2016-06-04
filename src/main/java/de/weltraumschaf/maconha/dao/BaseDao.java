package de.weltraumschaf.maconha.dao;


import de.weltraumschaf.commons.validate.Validate;
import java.util.Collection;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base implementation for data access objects.
 *
 * @param <T> managed entity type
 */
@Transactional
abstract class BaseDao<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);
    /**
     * Class managed by the DAO.
     */
    private final Class<T> persistentClass;
    /**
     * Injected by Spring.
     */
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Dedicated constructor.
     *
     * @param persistentClass must not be {@code null}
     */
    @SuppressWarnings("unchecked")
    public BaseDao(final Class<T> persistentClass) {
        super();
        this.persistentClass = Validate.notNull(persistentClass, "persistentClass");
    }

    /**
     * Get a database session.
     *
     * @return never {@code null}
     */
    protected final Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Find entity by ID.
     *
     * @param id the wanted ID
     * @return maybe {@code null}
     */
    @SuppressWarnings("unchecked")
    public final T findById(final int id) {
        return (T) getSession().get(persistentClass, id);
    }

    @SuppressWarnings("unchecked")
    public final Collection<T> findAll() {
        return (Collection<T>) createEntityCriteria().list();
    }

    public final void save(final T entity) {
        LOGGER.debug("Save entity: {}", entity);
        getSession().persist(entity);
    }

    public final void delete(final T entity) {
        getSession().delete(entity);
    }

    public void deleteById(final int id) {
        final T toDelete = findById(id);

        if (null != toDelete) {
            delete(toDelete);
        }
    }

    protected final Criteria createEntityCriteria() {
        return getSession().createCriteria(persistentClass);
    }
}
