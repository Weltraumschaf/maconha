package de.weltraumschaf.maconha.dao;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 */
abstract class BaseDao<T> {

    private final Class<T> persistentClass;
    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public BaseDao(final Class<T> persistentClass) {
        super();
        this.persistentClass = persistentClass;
    }


    protected final Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public final T findById(final int id) {
        return (T) getSession().get(persistentClass, id);
    }

    public final void save(final T entity) {
        getSession().persist(entity);
    }

    public final void delete(final T entity) {
        getSession().delete(entity);
    }

    protected final Criteria createEntityCriteria() {
        return getSession().createCriteria(persistentClass);
    }
}
