package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.core.FileNameExtractor;
import de.weltraumschaf.maconha.dao.KeywordDao;
import de.weltraumschaf.maconha.dao.MediaDao;
import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.model.OriginFile;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This job indexes imported media.
 */
final class GenerateIndex extends BaseJob<Void> {

    static final Description DESCRIPTION = new Description(GenerateIndex.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateIndex.class);
    private static final Collection<String> IGNORED_KEYWORDS = Arrays.asList(
        "i", "a", "an", "ve", "the", "this", "that", "who");
    private final FileNameExtractor extractor = new FileNameExtractor();
    @Autowired
    private MediaDao input;
    @Autowired
    private KeywordDao output;

    GenerateIndex() {
        super(generateName(GenerateIndex.class));
    }

    void setInput(final MediaDao input) {
        this.input = Validate.notNull(input, "input");
    }

    void setOutput(final KeywordDao output) {
        this.output = Validate.notNull(output, "output");
    }

    @Override
    public Void execute() throws Exception {
        final Collection<Media> allImportedMedia = input.findAll();
        allImportedMedia.stream().forEach(media -> index(media));
        return null;
    }

    @Override
    protected Description description() {
        return DESCRIPTION;
    }

    private void index(final Media media) {
        final OriginFile file = media.getOriginFile();

        if (null == file) {
            LOGGER.debug("Media has no origin file, ignoring ({}).", media);
            return;
        }

        // TODO Not only extract from file name, but also from title because will be editable.
        extract(file)
            .filter(literal -> wanted(literal))
            .forEach(literal -> save(literal, media));
    }

    private boolean wanted(final String literal) {
        return !IGNORED_KEYWORDS.contains(literal);
    }

    private Stream<String> extract(final OriginFile file) {
        return extractor.extractKeywords(file.getBaseDir(), file.getAbsolutePath()).stream();
    }

    private void save(final String literal, final Media media) {
        Keyword keyword;// = output.findByLiteral(literal);

//        if (null == keyword) {
//            LOGGER.debug("Create new keyword for literal '{}'.", literal);
            keyword = new Keyword().setLiteral(literal);
//        } else {
//            LOGGER.debug("Use existing keyword with id {} for literal '{}'.", keyword.getId(), literal);
//        }

        output.save(keyword.addMedias(media));
    }

}
