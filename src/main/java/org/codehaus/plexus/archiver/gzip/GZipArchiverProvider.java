package org.codehaus.plexus.archiver.gzip;

import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archivers.spi.ArchiverProvider;

public final class GZipArchiverProvider implements ArchiverProvider {

	@Override
	public String getName() {
		return "gzip";
	}

	@Override
	public Archiver newArchiver() {
		return new GZipArchiver();
	}

}
