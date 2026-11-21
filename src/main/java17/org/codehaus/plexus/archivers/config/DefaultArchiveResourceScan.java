package org.codehaus.plexus.archivers.config;

import java.nio.file.Path;
import java.util.Objects;

import org.codehaus.plexus.archiver.util.DefaultArchivedFileSet;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;

final class DefaultArchiveResourceScan implements ResourceScan.ArchiveScan {

	private final Path archive;
	private ArchiveEntryMatcher matcher = entry -> true;

	DefaultArchiveResourceScan(Path archive) {
		this.archive = Objects.requireNonNull(archive, "archive");
	}

	@Override
	public DefaultArchiveResourceScan matcher(ArchiveEntryMatcher matcher) {
		this.matcher = Objects.requireNonNull(matcher, "matcher");
		return this;
	}

	@Override
	public Path archive() {
		return archive;
	}

	ArchiveEntryMatcher matcher() {
		return matcher;
	}

	DefaultArchivedFileSet toArchivedFileSet() {
		DefaultArchivedFileSet fileSet = new DefaultArchivedFileSet(archive.toFile());

		// Exclusions are evaluated by the matcher, including any standard exclusions.
		// Disable legacy exclusions so they cannot override the matcher's result.
		fileSet.usingDefaultExcludes(false);

		ArchiveEntryMatcher entryMatcher = matcher;
		fileSet.setFileSelectors(
				new FileSelector[] { fileInfo -> entryMatcher.matches(new FileInfoArchiveEntry(fileInfo)) });

		return fileSet;
	}
}