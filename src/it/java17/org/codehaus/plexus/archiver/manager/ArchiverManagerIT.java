package org.codehaus.plexus.archiver.manager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipFile;

import org.codehaus.plexus.archiver.ArchiveCreator;
import org.codehaus.plexus.archiver.ArchiveExtractor;
import org.codehaus.plexus.archivers.config.ArchiverConfiguration;
import org.codehaus.plexus.archivers.config.ResourceScan;
import org.codehaus.plexus.archivers.config.UnArchiverConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class ArchiverManagerIT {

	@Test
	void configuredLookupsCreateAndExtractArchive(@TempDir Path tempDirectory)
	        throws Exception {
	    ArchiverManager manager = ArchiverManagers.create();

	    Path source = Files.createDirectory(tempDirectory.resolve("source"));
	    Files.writeString(source.resolve("message.txt"), "hello");

	    Path archive = tempDirectory.resolve("archive.zip");
	    Path destination = Files.createDirectory(tempDirectory.resolve("extracted"));

	    ArchiveCreator creator = manager.createArchiver(
	            ArchiverConfiguration.config(settings -> {
	                settings.addResource(
	                        ResourceScan.fromDirectory(source),
	                        emit -> emit.setPrefix("content/"));
	            }));

	    creator.createArchive(archive);

	    try (ZipFile zip = new ZipFile(archive.toFile())) {
	        assertThat(zip.getEntry("content/message.txt")).isNotNull();
	    }

	    ArchiveExtractor extractor = manager.createUnArchiver(
	            UnArchiverConfiguration.config(settings -> {
	                settings.setDestinationDirectory(destination);
	            }));

	    extractor.extractArchive(archive);

	    assertThat(Files.readString(destination.resolve("content/message.txt")))
	            .isEqualTo("hello");
	}

}
