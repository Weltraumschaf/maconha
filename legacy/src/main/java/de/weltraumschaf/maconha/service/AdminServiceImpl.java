package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.Media;
import java.util.Collection;
import java.util.Collections;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 */
@Transactional
@Service("admin")
final class AdminServiceImpl implements AdminService {

    @Override
    public Collection<Media> listAll() {
        return Collections.emptyList();
    }

    @Override
    public void startIndexing() {
        
    }

}
