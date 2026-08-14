package org.codehaus.plexus.archiver.ear;

import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archivers.spi.UnArchiverProvider;

public final class EarUnArchiverProvider implements UnArchiverProvider {

    @Override
    public String getName() {
        return "ear";
    }

    @Override
    public UnArchiver newUnarchiver() {
        return new EarUnArchiver();
    }
}
