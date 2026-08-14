package org.codehaus.plexus.archiver.manager;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archivers.spi.ArchiverProvider;
import org.codehaus.plexus.archivers.spi.PlexusIoResourceCollectionProvider;
import org.codehaus.plexus.archivers.spi.UnArchiverProvider;
import org.codehaus.plexus.components.io.resources.PlexusIoResourceCollection;

public class SpiArchiverManager extends AbstractArchiverManager {

    public SpiArchiverManager() {
        super(archivers(), unarchivers(), plexusIoResourceCollections());
    }

    private static Map<String, Supplier<Archiver>> archivers() {
        return ServiceLoader.load(ArchiverProvider.class).stream()
                .map(Provider::get)
                .collect(Collectors.toMap(ArchiverProvider::getName, k -> k::newArchiver));
    }

    private static Map<String, Supplier<UnArchiver>> unarchivers() {
        return ServiceLoader.load(UnArchiverProvider.class).stream()
                .map(Provider::get)
                .collect(Collectors.toMap(UnArchiverProvider::getName, k -> k::newUnarchiver));
    }

    private static Map<String, Supplier<PlexusIoResourceCollection>> plexusIoResourceCollections() {
        return ServiceLoader.load(PlexusIoResourceCollectionProvider.class).stream()
                .map(Provider::get)
                .collect(Collectors.toMap(
                        PlexusIoResourceCollectionProvider::getName, k -> k::newPlexusIoResourceCollection));
    }
}
