package de.weltraumschaf.maconha.core;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Walks file tree and collects files with wanted extension.
 */
final class FileFinder extends SimpleFileVisitor<Path> {

    private final Collection<Path> collectedFiles = new ArrayList<>();
    private final Set<? extends FileExtension> wantedFiles;

    public FileFinder(final Set<? extends FileExtension> wantedFiles) {
        super();
        this.wantedFiles = wantedFiles;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        if (hasExtensionToCollect(file)) {
            collectedFiles.add(file);
        }

        return super.visitFile(file, attrs);
    }

    public Collection<Path> getCollectedFiles() {
        return new ArrayList<>(collectedFiles);
    }


    boolean hasExtensionToCollect(final Path file) {
        if (null == file) {
            return false;
        }


        final String fileName = file.toString();

        if (fileName.trim().isEmpty()) {
            return false;
        }

        final String extension = FileExtension.extractExtension(fileName);
        return wantedFiles.stream().anyMatch((wanted) -> (wanted.isExtension(extension)));
    }

    public static Collection<Path> find(final Path root, final Set<? extends FileExtension> wantedFiles) throws IOException {
        final FileFinder finder = new FileFinder(wantedFiles);
        Files.walkFileTree(root, finder);
        return finder.getCollectedFiles();
    }
}
