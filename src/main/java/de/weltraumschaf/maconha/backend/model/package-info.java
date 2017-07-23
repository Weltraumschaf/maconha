/**
 * This package contains the whole <a href="https://ci.weltraumschaf.de/job/maconha/site/05_building_block_view.html#domain_model">
 * domain model</a>.
 *
 * <h3>Important Notes for Implementations</h3>
 *
 * <h4>Non-Final Classes</h4>
 *
 * <p>
 * JPA model entities must never be {@code final}. This is necessary because the implementing frameworks replaces the
 * objects with proxies to make lazy loading possible. Also they must provide always a default constructor w/o any
 * arguments. Despite that the {@link java.lang.Object#equals(java.lang.Object)} and {@link java.lang.Object#hashCode()}
 * should <a href="http://www.artima.com/lejava/articles/equality.html">always be final</a>.
 * </p>
 *
 * <h4>Which Fields to Consider in Equals and Hashcode</h4>
 *
 * <p>
 * The {@link javax.persistence.Id primary key} should not be used in {@link java.lang.Object#equals(java.lang.Object)}
 * and {@link java.lang.Object#hashCode()} because entities must provide a consistent equality for all JPA states. But
 * most JPA implementations set the primary key when hte entity is merged. So this field changes. Instead a so called
 * business key should be used as described
 * <a href="https://vladmihalcea.com/2013/10/23/hibernate-facts-equals-and-hashcode/">here</a> and in hte
 * <a href="https://developer.jboss.org/wiki/EqualsAndHashCode?_sscc=t">Hibernate documentation</a>. If it is required
 * to use the primary key for equality a solution is described <a href=
 * "https://vladmihalcea.com/2016/06/06/how-to-implement-equals-and-hashcode-using-the-entity-identifier/">here</a>.
 * </p>
 */
package de.weltraumschaf.maconha.backend.model;