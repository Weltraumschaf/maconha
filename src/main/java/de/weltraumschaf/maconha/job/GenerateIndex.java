package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.core.FileNameExtractor;
import de.weltraumschaf.maconha.dao.KeywordDao;
import de.weltraumschaf.maconha.dao.MediaDao;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.model.OriginFile;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 */
public final class GenerateIndex extends BaseJob<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateIndex.class);
    private final FileNameExtractor extractor = new FileNameExtractor();
    @Autowired
    private MediaDao input;
    @Autowired
    private KeywordDao output;

    public GenerateIndex() {
        super(generateName(GenerateIndex.class));
    }

    @Override
    public Void execute() throws Exception {
        input.findAll().forEach(media -> index(media));

        return null;
    }

    private void index(final Media media) {
        final OriginFile file = media.getOriginFile();
        final Collection<String> extractedKeywords = extractor.extractKeywords(file.getAbsolutePath(), file.getAbsolutePath());
    }

}
