package de.weltraumschaf.maconha.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base interface for repos to define one common type for IDs.
 *
 * @param <E> type of entity
 */
@NoRepositoryBean
interface BaseRepo<E> extends JpaRepository<E, Integer> {

}
