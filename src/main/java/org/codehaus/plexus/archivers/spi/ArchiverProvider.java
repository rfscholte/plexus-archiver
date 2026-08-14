package org.codehaus.plexus.archivers.spi;

import org.codehaus.plexus.archiver.Archiver;

public interface ArchiverProvider {

    String getName();

    Archiver newArchiver();
}
