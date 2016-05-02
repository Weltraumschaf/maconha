package de.weltraumschaf.maconha.dao;

import java.io.Serializable;

import java.lang.reflect.ParameterizedType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 */
abstract class AbstractDao<P extends Serializable, T> {

    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public AbstractDao(final Class<T> persistentClass) {
        super();
        this.persistentClass = persistentClass;
    }

    @Autowired
    private SessionFactory sessionFactory;

    protected final Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public final T getByKey(final P key) {
        return (T) getSession().get(persistentClass, key);
    }

    public final void persist(final T entity) {
        getSession().persist(entity);
    }

    public final void delete(final T entity) {
        getSession().delete(entity);
    }

    protected final Criteria createEntityCriteria() {
        return getSession().createCriteria(persistentClass);
    }
}
