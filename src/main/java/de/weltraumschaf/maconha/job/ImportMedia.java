package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.dao.MediaDao;
import de.weltraumschaf.maconha.dao.OriginFileDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * THis job import scanned media files.
 */
public final class ImportMedia extends BaseJob<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportMedia.class);
    @Autowired
    private OriginFileDao input;
    @Autowired
    private MediaDao output;

    public ImportMedia() {
        super(generateName(ImportMedia.class));
    }

    public void setInput(final OriginFileDao input) {
        this.input = Validate.notNull(input, "input");
    }

    public void setOutput(final MediaDao output) {
        this.output = Validate.notNull(output, "output");
    }

    @Override
    protected Void execute() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
