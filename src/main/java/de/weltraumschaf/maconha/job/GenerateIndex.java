package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.core.FileNameExtractor;
import de.weltraumschaf.maconha.core.IgnoredKeywords;
import de.weltraumschaf.maconha.core.MalformedKeywords;
import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.model.OriginFile;
import de.weltraumschaf.maconha.repos.KeywordRepo;
import de.weltraumschaf.maconha.repos.MediaRepo;
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
    private final FileNameExtractor extractor = new FileNameExtractor();
    @Autowired
    private MediaRepo input;
    @Autowired
    private KeywordRepo output;

    GenerateIndex() {
        super(generateName(GenerateIndex.class));
    }

    void setInput(final MediaRepo input) {
        this.input = Validate.notNull(input, "input");
    }

    void setOutput(final KeywordRepo output) {
        this.output = Validate.notNull(output, "output");
    }

    @Override
    public Void execute() throws Exception {
        final Collection<Media> allImportedMedia = input.findAll();
        begin(allImportedMedia.size());
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
            .filter(new IgnoredKeywords())
            .filter(new MalformedKeywords())
            .forEach(literal -> save(literal, media));
        worked(1);
    }

    private Stream<String> extract(final OriginFile file) {
        return extractor.extractKeywords(file.getBaseDir(), file.getAbsolutePath()).stream();
    }

    private void save(final String literal, final Media media) {
        LOGGER.debug("Save keyword '{}' to media with id {}", literal, media.getId());
        Keyword keyword = output.findByLiteral(literal);

        if (null == keyword) {
            LOGGER.debug("Create new keyword for literal '{}'.", literal);
            keyword = new Keyword().setLiteral(literal);
            output.save(keyword);
        } else {
            LOGGER.debug("Use existing keyword with id {} for literal '{}'.", keyword.getId(), literal);
        }

        media.addKeyword(keyword);
        input.save(media);
    }

}
