package org.codehaus.plexus.archivers;

import java.io.IOException;
import java.nio.file.Path;

import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;

public interface ArchiveCreator {

	void createArchive(Path destinationFile) throws NoSuchArchiverException, IOException;
}
