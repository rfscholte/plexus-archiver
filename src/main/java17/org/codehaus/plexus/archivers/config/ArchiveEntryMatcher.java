package org.codehaus.plexus.archivers.config;

@FunctionalInterface
public interface ArchiveEntryMatcher {

	boolean matches(ArchiveEntryInfo archiveEntry);
}
