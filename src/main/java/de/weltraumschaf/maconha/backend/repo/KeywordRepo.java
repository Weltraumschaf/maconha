package de.weltraumschaf.maconha.backend.repo;

import de.weltraumschaf.maconha.backend.model.entity.Keyword;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * Auto implemented by Spring.
 */
public interface KeywordRepo extends BaseRepo<Keyword> {

    Keyword findByLiteral(String literal);

    List<Keyword> findByLiteralLike(String literal);

    Collection<Keyword> findByLiteralIn(Collection<String> literals);

    /**
     * Get the top ten keywords: With most media files.
     *
     * @return {@literal [java.lang.String, java.math.BigInteger]}
     */
    @Query(value = "SELECT " +
        "  kw.literal AS keyword, " +
        "  count(1) AS files " +
        "FROM Keyword AS kw " +
        "  JOIN Keyword_MediaFile AS kmf ON (kw.id = kmf.keyword_id) " +
        "  JOIN MediaFile AS mf ON (mf.id = kmf.media_id) " +
        "GROUP BY kw.id " +
        "ORDER BY files DESC " +
        "LIMIT 10", nativeQuery = true)
    Collection<Object[]> topTen();
}
