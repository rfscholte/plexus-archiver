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

import java.util.Objects;

import org.codehaus.plexus.components.io.fileselectors.FileInfo;

/**
 * Adapts Plexus IO entry metadata to the dependency-free matcher API.
 *
 * <p>Delegates to the underlying entry without copying its data.</p>
 *
 * @param fileInfo the entry metadata supplied by Plexus IO
 * @since 5.0.0
 */
record FileInfoArchiveEntry(FileInfo fileInfo) implements ArchiveEntryInfo {

    FileInfoArchiveEntry {
        Objects.requireNonNull(fileInfo, "fileInfo");
    }

    @Override
    public String name() {
        return fileInfo.getName();
    }

    @Override
    public boolean isFile() {
        return fileInfo.isFile();
    }

    @Override
    public boolean isDirectory() {
        return fileInfo.isDirectory();
    }

    @Override
    public boolean isSymbolicLink() {
        return fileInfo.isSymbolicLink();
    }
}