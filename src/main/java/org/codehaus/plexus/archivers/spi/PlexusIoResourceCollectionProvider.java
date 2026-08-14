package org.codehaus.plexus.archivers.spi;

import org.codehaus.plexus.components.io.resources.PlexusIoResourceCollection;

public interface PlexusIoResourceCollectionProvider {

    String getName();

    PlexusIoResourceCollection newPlexusIoResourceCollection();
}
