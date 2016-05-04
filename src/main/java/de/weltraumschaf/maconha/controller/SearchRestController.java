package de.weltraumschaf.maconha.controller;

import de.weltraumschaf.maconha.core.Validator;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.service.SearchService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("/search/api")
public final class SearchRestController {

    private final Validator validator = new Validator();
    /**
     * Service which will do all data retrieval/manipulation work
     */
    @Autowired
    private SearchService search;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<Collection<Media>> search(final @RequestParam(value = "q") String query) {
        return new ResponseEntity<>(search.search(validator.cleanSearchQuery(query)), HttpStatus.OK);
    }
}
