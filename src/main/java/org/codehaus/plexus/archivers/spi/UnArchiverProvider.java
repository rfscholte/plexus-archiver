package org.codehaus.plexus.archivers.spi;

import org.codehaus.plexus.archiver.UnArchiver;

public interface UnArchiverProvider {

    String getName();

    UnArchiver newUnarchiver();
}
