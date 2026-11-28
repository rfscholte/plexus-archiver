/*
 * Copyright MojoHaus and Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.codehaus.plexus.archivers.config;

import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import org.codehaus.plexus.archiver.ArchivedFileSet;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.BaseFileSet;
import org.codehaus.plexus.archiver.FileSet;
import org.codehaus.plexus.archiver.util.AbstractFileSet;
import org.codehaus.plexus.archiver.util.DefaultArchivedFileSet;
import org.codehaus.plexus.archiver.util.DefaultFileSet;

final class DefaultArchiverSettings implements ArchiverSettings {

	private List<BaseFileSet> baseFileSets = new ArrayList<BaseFileSet>();
	private Optional<Path> destFile = Optional.empty();
	private Optional<EmptyDirectoryHandling> emptyDirectoryHandling = Optional.empty(); 
	private Optional<ArchiveCreation> archiveCreation = Optional.empty();
	private Optional<DuplicateHandling> duplicateHandling = Optional.empty();
	private Optional<PermissionPolicy> permissionPolicy = Optional.empty();

    @Override
    public void addResource(ResourceScan scan, Consumer<ResourceEmitSettings> emit) {
        Objects.requireNonNull(scan, "scan");
        Objects.requireNonNull(emit, "emit");

        AbstractFileSet<?> fileSet;
        if (scan instanceof DefaultDirectoryResourceScan directoryScan) {
        	fileSet = directoryScan.toFileSet();
        } else if (scan instanceof DefaultArchiveResourceScan archiveScan) {
        	fileSet = archiveScan.toArchivedFileSet();
        } else {
            throw new IncompatibleClassChangeError(
                    "Unsupported ResourceScan implementation: " + scan.getClass().getName());
        }

        DefaultResourceEmitSettings resourceEmitSettings = new DefaultResourceEmitSettings();
        emit.accept(resourceEmitSettings);

        resourceEmitSettings.apply(fileSet);
        baseFileSets.add(fileSet);

    }

    @Override
    public void setEmptyDirectoryHandling(EmptyDirectoryHandling handling) {
    	this.emptyDirectoryHandling = Optional.of(handling);
    }

    @Override
    public void setArchiveCreation(ArchiveCreation archiveCreation) {
    	this.archiveCreation = Optional.of(archiveCreation);
    }

    @Override
    public void setDuplicateHandling(DuplicateHandling duplicateHandling) {
    	this.duplicateHandling = Optional.of(duplicateHandling);
    }

    @Override
    public void setPermissionPolicy(PermissionPolicy policy) {
    	this.permissionPolicy = Optional.of(policy);
    }

    private void applyIgnorePolicy(Archiver archiver) {
        archiver.setIgnorePermissions(true);
        archiver.setUmask(-1);
    }

    private void applyUmaskPolicy(Archiver archiver, UmaskPermissionPolicy policy) {
    	archiver.setUmask(toMode(policy.mask()));
    }

    private void applyEntryModesPolicy(Archiver archiver, EntryModesPermissionPolicy policy) {
    	archiver.setUmask(-1);
        applyFileMode(archiver, policy.files());
        applyDirectoryMode(archiver, policy.directories());
    }

    private void applyFileMode(Archiver archiver,FileMode fileMode) {
        if (fileMode instanceof OverrideFileMode mode) {
            archiver.setFileMode(toMode(mode.permissions()));
        }
        if (fileMode instanceof FallbackFileMode mode) {
            archiver.setDefaultFileMode(toMode(mode.permissions()));
            return;
        }
        if (!(fileMode instanceof InheritedFileMode)) {
        	throw new IncompatibleClassChangeError(
                "Unsupported FileMode implementation: " + fileMode.getClass().getName());
        }
    }

    private void applyDirectoryMode(Archiver archiver, DirectoryMode directoryMode) {
        if (directoryMode instanceof OverrideDirectoryMode mode) {
            archiver.setDirectoryMode(toMode(mode.permissions()));
            return;
        }
        if (directoryMode instanceof FallbackDirectoryMode mode) {
            archiver.setDefaultDirectoryMode(toMode(mode.permissions()));
            return;
        }
        if (!(directoryMode instanceof InheritedDirectoryMode)) {
            throw new IncompatibleClassChangeError(
                    "Unsupported DirectoryMode implementation: " + directoryMode.getClass().getName());
        }
    }

    private static int toMode(FilePermissions filePermissions) {
        if (filePermissions instanceof PosixPermissions permissions) {
            int mode = 0;
            for (PosixFilePermission permission : permissions.permissions()) {
                mode |= switch (permission) {
                    case OWNER_READ -> 0400;
                    case OWNER_WRITE -> 0200;
                    case OWNER_EXECUTE -> 0100;
                    case GROUP_READ -> 0040;
                    case GROUP_WRITE -> 0020;
                    case GROUP_EXECUTE -> 0010;
                    case OTHERS_READ -> 0004;
                    case OTHERS_WRITE -> 0002;
                    case OTHERS_EXECUTE -> 0001;
                };
            }
            return mode;
        }

        if (filePermissions instanceof ModePermissions permissions) {
            return permissions.mode();
        }

        throw new IncompatibleClassChangeError(
                "Unsupported FilePermissions implementation: " + filePermissions.getClass().getName());
    }
    
    void apply(Archiver archiver) {
    	destFile.map(Path::toFile).ifPresent(archiver::setDestFile);
    	emptyDirectoryHandling.ifPresent(h -> archiver.setIncludeEmptyDirs(h == EmptyDirectoryHandling.INCLUDE));
    	archiveCreation.ifPresent(h -> archiver.setForced(h == ArchiveCreation.ALWAYS));
    	duplicateHandling.ifPresent(h -> {
            String behavior = h == DuplicateHandling.ADD ? Archiver.DUPLICATES_ADD
    						: h == DuplicateHandling.PRESERVE ? Archiver.DUPLICATES_PRESERVE
    						: h == DuplicateHandling.SKIP ? Archiver.DUPLICATES_SKIP
							: Archiver.DUPLICATES_FAIL;
            archiver.setDuplicateBehavior(behavior);
    	});
    	permissionPolicy.ifPresent(policy -> {
            if (policy instanceof IgnorePermissionPolicy) {
                applyIgnorePolicy(archiver);
                return;
            }
            if (policy instanceof UmaskPermissionPolicy umaskPolicy) {
                applyUmaskPolicy(archiver, umaskPolicy);
                return;
            }
            if (policy instanceof EntryModesPermissionPolicy entryModesPolicy) {
                applyEntryModesPolicy(archiver, entryModesPolicy);
                return;
            }
    	});

    	
    	for(BaseFileSet baseFileSet : baseFileSets) {
    		if(baseFileSet instanceof FileSet s) {
    			archiver.addFileSet(s);
    		} else if (baseFileSet instanceof ArchivedFileSet s) {
    			archiver.addArchivedFileSet(s);
    		}
    	}
    }
    
}