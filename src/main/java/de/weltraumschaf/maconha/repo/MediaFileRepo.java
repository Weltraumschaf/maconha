package de.weltraumschaf.maconha.repo;

import de.weltraumschaf.maconha.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Auto implemented by Spring.
 */
public interface MediaFileRepo extends BaseRepo<MediaFile>, JpaSpecificationExecutor<MediaFile> {

    @Override
    Page<MediaFile> findAll(Pageable pageable);

    MediaFile findByRelativeFileName(String relativeFileName);

    MediaFile findByRelativeFileNameAndBucket(String relativeFileName, Bucket bucket);

    @Query("select m1 from MediaFile m1 where m1.fileHash in (select m2.fileHash from MediaFile m2 group by m2.fileHash having count(m2) > 1) ORDER BY m1.fileHash")
    List<MediaFile> findDuplicates();

    List<MediaFile> findByKeywords(Keyword value);

    /**
     * Class to create search specifications to find {@link MediaFile}.
     * <p>
     * Based on <a href="https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-four-jpa-criteria-queries/">
     * this tutorial</a>.
     * </p>
     */
    final class MediaFileSpecifications {

        private MediaFileSpecifications() {
            super();
        }

        static public Specification<MediaFile> relativeFileNameIgnoreCaseAndTypeAndFormat(final String relativeFileName, final MediaType type, final FileExtension format) {
            return (root, query, cb) -> {
                final String containsLikePattern = getContainsLikePattern(relativeFileName);
                final Collection<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.like(cb.lower(root.get(MediaFile_.relativeFileName)), containsLikePattern));

                if (type != null) {
                    predicates.add(cb.equal(root.get(MediaFile_.type), type));
                }

                if (format != null) {
                    predicates.add(cb.equal(root.get(MediaFile_.format), format));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        }

        static private String getContainsLikePattern(String searchTerm) {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return "%";
            } else {
                return "%" + searchTerm.trim().toLowerCase() + "%";
            }
        }
    }
}
