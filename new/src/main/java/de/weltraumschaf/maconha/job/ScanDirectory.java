package de.weltraumschaf.maconha.job;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public final class ScanDirectory extends BaseJob<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanDirectory.class);

    public ScanDirectory() {
        super(generateName(ScanDirectory.class));
    }

    @Override
    public Void execute() throws Exception {
        for (int i = 0; i < 500; ++i) {
            if (isCanceled()) {
                return null;
            }

            emmit("Generate index %d ...", i);
            TimeUnit.MILLISECONDS.sleep(100);
        }

        return null;
    }
}
