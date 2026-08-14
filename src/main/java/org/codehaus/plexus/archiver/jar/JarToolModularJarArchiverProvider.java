package org.codehaus.plexus.archiver.jar;

import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archivers.spi.ArchiverProvider;

public class JarToolModularJarArchiverProvider implements ArchiverProvider {

    @Override
    public String getName() {
        return "mjar";
    }

    @Override
    public Archiver newArchiver() {
        return new JarToolModularJarArchiver();
    }
}
