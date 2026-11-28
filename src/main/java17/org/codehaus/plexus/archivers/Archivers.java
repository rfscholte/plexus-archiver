package org.codehaus.plexus.archivers;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.codehaus.plexus.archiver.manager.FileNames;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.archiver.manager.ServiceLoaderArchiverManager;
import org.codehaus.plexus.archivers.config.ArchiverConfiguration;
import org.codehaus.plexus.archivers.config.UnArchiverConfiguration;

public final class Archivers {

    private Archivers() {}

	/**
	 * Returns an archiver factory selected from the file extension.
	 *
	 * @since 5.0.0
	 */
	@Nonnull
	public static ArchiveCreator createArchiver(ArchiverConfiguration configuration)
			throws NoSuchArchiverException {
		Objects.requireNonNull(configuration, "configuration");
		ServiceLoaderArchiverManager manager = new ServiceLoaderArchiverManager();
		
		return destFile ->  {
			Objects.requireNonNull(destFile, "destFile");
			String format = FileNames.getFileExtension(destFile);

			manager.getArchiverFactory(format)
				.create(configuration)
				.createArchive(destFile);
		};
	}

	/**
	 * Returns an unarchiver factory selected from the file extension.
	 *
	 * @since 5.0.0
	 */
	@Nonnull
	public static ArchiveExtractor createUnArchiver(UnArchiverConfiguration configuration)
			throws NoSuchArchiverException {
		Objects.requireNonNull(configuration, "configuration");
		ServiceLoaderArchiverManager manager = new ServiceLoaderArchiverManager();
		
		return srcFile -> {
			Objects.requireNonNull(srcFile, "srcFile");
			String format = FileNames.getFileExtension(srcFile);

			manager.getUnArchiverFactory(format)
				.create(configuration)
				.extractArchive(srcFile);
		};
	}
}
