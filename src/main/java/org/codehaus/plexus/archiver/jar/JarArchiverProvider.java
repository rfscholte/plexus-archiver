package org.codehaus.plexus.archiver.jar;

import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archivers.spi.ArchiverProvider;

public class JarArchiverProvider implements ArchiverProvider {

    @Override
    public String getName() {
        return "jar";
    }

    @Override
    public Archiver newArchiver() {
        return new JarArchiver();
    }
}
