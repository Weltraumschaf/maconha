package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.model.TestData;
import java.util.Collection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 */
@Transactional
@Service("search")
final class SearchServiceImpl implements SearchService {

    @Override
    public Collection<Media> search(String query) {
        return new TestData().generate(7);
    }

}
