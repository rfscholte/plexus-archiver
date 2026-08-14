package org.codehaus.plexus.archiver.bzip2;

import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archivers.spi.ArchiverProvider;

public final class BZip2ArchiverProvider implements ArchiverProvider {

    @Override
    public String getName() {
        return "bzip2";
    }

    @Override
    public Archiver newArchiver() {
        return new BZip2Archiver();
    }
}
