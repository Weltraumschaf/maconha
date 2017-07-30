package de.weltraumschaf.maconha.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * BeanLocator is singleton Spring bean that is capable of finding required beans from Spring's application context.
 */
@Component
@Deprecated
public class BeanLocator {

    private final ApplicationContext context;

    @Autowired
    public BeanLocator(final ApplicationContext context) {
        super();
        this.context = context;
    }

    /**
     * Looks up a bean of the given type.
     *
     * @param <T> type of bean
     * @param beanType the type to lookup
     * @return an autowired instance of the given type
     */
    public <T> T find(final Class<T> beanType) {
        return context.getBean(beanType);
    }

}
