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

/**
 * Read-only entry metadata used when selecting archive contents.
 *
 * <p>This interface exposes no archive-format or resource-library types.</p>
 *
 * @since 5.0.0
 */
public interface ArchiveEntryInfo {

    /**
     * Returns the entry name relative to the archive root, before output
     * prefixes or file-name mappings are applied.
     *
     * @return the archive-relative entry name
     */
    String name();

    /**
     * Returns whether the entry represents a file.
     *
     * @return {@code true} if the entry represents a file
     */
    boolean isFile();

    /**
     * Returns whether the entry represents a directory.
     *
     * @return {@code true} if the entry represents a directory
     */
    boolean isDirectory();

    /**
     * Returns whether the entry represents a symbolic link.
     *
     * @return {@code true} if the entry represents a symbolic link
     */
    boolean isSymbolicLink();
}