package org.codehaus.plexus.archiver.ear;

import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archivers.spi.ArchiverProvider;

public final class EarArchiverProvider implements ArchiverProvider {

    @Override
    public String getName() {
        return "ear";
    }

    @Override
    public Archiver newArchiver() {
        return new EarArchiver();
    }
}
