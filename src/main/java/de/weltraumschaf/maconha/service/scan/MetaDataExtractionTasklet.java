package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.maconha.model.*;
import de.weltraumschaf.maconha.repo.BucketRepo;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.scan.extraction.*;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Tasklet to extract metadata from found files.
 */
final class MetaDataExtractionTasklet implements Tasklet {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetaDataExtractionTasklet.class);

    private final JobParamRetriever params = new JobParamRetriever();
    private final BucketRepo buckets;
    private final MediaFileRepo mediaFiles;
    private final KeywordRepo keywords;

    MetaDataExtractionTasklet(final BucketRepo buckets, final MediaFileRepo mediaFiles, final KeywordRepo keywords) {
        super();
        this.buckets = buckets;
        this.mediaFiles = mediaFiles;
        this.keywords = keywords;
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext ctx) throws Exception {
        final Set<HashedFile> unseenFiles = params.retrieveUnseenFiles(ctx);
        LOGGER.debug("Received {} hashed files to extract meta data.", unseenFiles.size());
        final Bucket bucket = buckets.findById(params.retrieveBucketId(ctx));
        unseenFiles.forEach(file -> extractMetaData(bucket, file));
        return RepeatStatus.FINISHED;
    }

    private void extractMetaData(final Bucket bucket, final HashedFile file) {
        LOGGER.debug("Extract meta data for: {}", file.getFile());
        final FileExtension extension;

        try {
            extension = extractExtension(file);
        } catch (final IllegalArgumentException e) {
            LOGGER.warn(e.getMessage());
            LOGGER.warn("Skipping file {}", file.getFile());
            return;
        }

        try {
            final MetaDataExtractor metaDataExtractor = new MetaDataExtractor();
            final Path absoluteFile = Paths.get(bucket.getDirectory()).resolve(file.getFile());
            final FileMetaData fileMetaData = metaDataExtractor.extract(absoluteFile.toString());

            final MediaFile media = new MediaFile();
            media.setType(MediaType.forValue(extension));
            media.setFormat(fileMetaData.getMime());
            media.setRelativeFileName(file.getFile());
            media.setFileHash(file.getHash());
            media.setBucket(bucket);

            final KeywordExtractor fileNameKeywordsExtractor = new KeywordsFromFileNameExtractor();
            final KeywordExtractor metaDataKeywordsExtractor = new KeywordsFromMetaDataExtractor();
            final Collection<String> foundKeywords = new HashSet<>();

            Collection<String> extracted = fileNameKeywordsExtractor.extract(file.getFile());
            LOGGER.debug("Extracted {} keywords from file name.", extracted.size());
            foundKeywords.addAll(extracted);
            extracted = metaDataKeywordsExtractor.extract(fileMetaData.getData());
            LOGGER.debug("Extracted {} keywords from file meta data.", extracted.size());
            foundKeywords.addAll(extracted);

            LOGGER.debug("Filter and store {} keywords.", foundKeywords.size());
            foundKeywords.stream()
                .filter(new MalformedKeywords())
                .filter(new IgnoredKeywords())
                .map(literal -> {
                    Keyword keyword = keywords.findByLiteral(literal);

                    if (null == keyword) {
                        LOGGER.debug("Save new keyword '{}'.", literal);
                        keyword = new Keyword();
                        keyword.setLiteral(literal);
                        keywords.save(keyword);
                    }

                    return keyword;
                }).forEach(media::addKeyword);

            mediaFiles.save(media);
        } catch (final Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    FileExtension extractExtension(final HashedFile file) {
        return FileExtension.forValue(FileExtension.extractExtension(file.getFile()));
    }
}
