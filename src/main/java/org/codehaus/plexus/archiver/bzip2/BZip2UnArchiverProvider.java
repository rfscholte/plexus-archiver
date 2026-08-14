package org.codehaus.plexus.archiver.bzip2;

import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archivers.spi.UnArchiverProvider;

public final class BZip2UnArchiverProvider implements UnArchiverProvider {

    @Override
    public String getName() {
        return "bzip2";
    }

    @Override
    public UnArchiver newUnarchiver() {
        return new BZip2UnArchiver();
    }
}
