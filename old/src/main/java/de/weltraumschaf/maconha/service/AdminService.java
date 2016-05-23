package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.Media;
import java.util.Collection;

/**
 */
public interface AdminService {

    Collection<Media> listAll();
    void startIndexing();

}
