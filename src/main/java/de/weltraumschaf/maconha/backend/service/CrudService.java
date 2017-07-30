package de.weltraumschaf.maconha.backend.service;

import de.weltraumschaf.maconha.backend.model.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CrudService<T extends BaseEntity> {

    CrudRepository<T, Long> getRepository();

    default T save(T entity) {
        return getRepository().save(entity);
    }

    default void delete(long id) {
        getRepository().delete(id);
    }

    default long count() {
        return getRepository().count();
    }

    default T load(long id) {
        return getRepository().findOne(id);
    }

    long countAnyMatching(Optional<String> filter);

    Page<T> findAnyMatching(Optional<String> filter, Pageable pageable);

    Page<T> find(Pageable pageable);
}
