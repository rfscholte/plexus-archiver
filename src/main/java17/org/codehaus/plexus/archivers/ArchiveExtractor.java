package org.codehaus.plexus.archivers;

import java.nio.file.Path;

import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;

public interface ArchiveExtractor {

	void extractArchive(Path sourceFile) throws NoSuchArchiverException;
}
