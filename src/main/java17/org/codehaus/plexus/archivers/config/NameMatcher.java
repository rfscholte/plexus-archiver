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

import java.nio.file.PathMatcher;

/**
 * Selects paths and archive entries using the same name-selection rules.
 *
 * <p>Paths are matched relative to the scan's base directory. Archive entries
 * are matched using their archive-relative names before output mappings or
 * prefixes are applied.</p>
 *
 * @since 5.0.0
 */
public interface NameMatcher extends PathMatcher, ArchiveEntryMatcher {
}