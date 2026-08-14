package org.codehaus.plexus.archiver.manager;

import javax.annotation.Nonnull;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.components.io.resources.PlexusIoResourceCollection;

import static java.util.Objects.requireNonNull;
import static org.codehaus.plexus.archiver.manager.ArchiverManagerUtils.getFileExtension;

abstract class AbstractArchiverManager implements ArchiverManager {

    private final Map<String, Supplier<Archiver>> archivers;

    private final Map<String, Supplier<UnArchiver>> unArchivers;

    private final Map<String, Supplier<PlexusIoResourceCollection>> plexusIoResourceCollections;

    protected AbstractArchiverManager(
            Map<String, Supplier<Archiver>> archivers,
            Map<String, Supplier<UnArchiver>> unArchivers,
            Map<String, Supplier<PlexusIoResourceCollection>> plexusIoResourceCollections) {
        this.archivers = Map.copyOf(archivers);
        this.unArchivers = Map.copyOf(unArchivers);
        this.plexusIoResourceCollections = Map.copyOf(plexusIoResourceCollections);
    }

    @Override
    @Nonnull
    public final Archiver getArchiver(@Nonnull String archiverName) throws NoSuchArchiverException {
        requireNonNull(archiverName);
        Supplier<Archiver> archiver = archivers.get(archiverName);
        if (archiver == null) {
            throw new NoSuchArchiverException(archiverName);
        }
        return archiver.get();
    }

    @Override
    @Nonnull
    public final UnArchiver getUnArchiver(@Nonnull String unArchiverName) throws NoSuchArchiverException {
        requireNonNull(unArchiverName);
        Supplier<UnArchiver> unArchiver = unArchivers.get(unArchiverName);
        if (unArchiver == null) {
            throw new NoSuchArchiverException(unArchiverName);
        }
        return unArchiver.get();
    }

    @Override
    @Nonnull
    public final PlexusIoResourceCollection getResourceCollection(String resourceCollectionName)
            throws NoSuchArchiverException {
        requireNonNull(resourceCollectionName);
        Supplier<PlexusIoResourceCollection> resourceCollection =
                plexusIoResourceCollections.get(resourceCollectionName);
        if (resourceCollection == null) {
            throw new NoSuchArchiverException(resourceCollectionName);
        }
        return resourceCollection.get();
    }

    @Override
    @Nonnull
    public final Archiver getArchiver(@Nonnull File file) throws NoSuchArchiverException {
        return getArchiver(getFileExtension(file));
    }

    @Override
    public Collection<String> getAvailableArchivers() {
        return archivers.keySet();
    }

    @Override
    @Nonnull
    public final UnArchiver getUnArchiver(@Nonnull File file) throws NoSuchArchiverException {
        return getUnArchiver(getFileExtension(file));
    }

    @Nonnull
    @Override
    public final Collection<String> getAvailableUnArchivers() {
        return unArchivers.keySet();
    }

    @Override
    @Nonnull
    public final PlexusIoResourceCollection getResourceCollection(@Nonnull File file) throws NoSuchArchiverException {
        return getResourceCollection(getFileExtension(file));
    }

    @Nonnull
    @Override
    public final Collection<String> getAvailableResourceCollections() {
        return plexusIoResourceCollections.keySet();
    }
}
