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

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import org.codehaus.plexus.components.io.fileselectors.IncludeExcludeFileSelector;

/**
 * Builds name matchers from inclusion and exclusion patterns.
 *
 * <p>Matching is case-sensitive and applies Plexus standard exclusions by
 * default. Names must match an inclusion pattern and no exclusion pattern.
 * An empty inclusion list matches nothing.</p>
 *
 * <p>This builder is mutable and is not intended for concurrent use.
 * Each invocation of {@link #toMatcher()} captures an independent snapshot
 * of its configuration.</p>
 *
 * @since 5.0.0
 */
public final class NamePatternBuilder {

    private final String[] includes;
    private String[] excludes = new String[0];
    private CaseSensitivity caseSensitivity = CaseSensitivity.SENSITIVE;
    private List<StandardExcludes.ExcludeGroup> standardExcludes =
            List.of(StandardExcludes.PLEXUS);

    private NamePatternBuilder(String[] includes) {
        this.includes = copyPatterns(includes);
    }

    /**
     * Creates a builder with the supplied inclusion patterns.
     *
     * @param includes the inclusion patterns
     * @return a new builder
     */
    public static NamePatternBuilder includes(String... includes) {
        return new NamePatternBuilder(includes);
    }

    /**
     * Replaces the explicit exclusion patterns.
     *
     * @param excludes the exclusion patterns
     * @return this builder
     */
    public NamePatternBuilder excludes(String... excludes) {
        this.excludes = copyPatterns(excludes);
        return this;
    }

    /**
     * Replaces the standard exclusion policy.
     *
     * @param mode the exclusion mode
     * @return this builder
     */
    public NamePatternBuilder standardExcludes(
            StandardExcludes.ExcludeMode mode) {
        Objects.requireNonNull(mode, "mode");
        if (!StandardExcludes.NONE.equals(mode)) {
            throw new IllegalArgumentException(
                    "Unsupported standard exclusion mode: " + mode);
        }

        standardExcludes = List.of();
        return this;
    }

    /**
     * Replaces the standard exclusion groups.
     *
     * <p>Supplying no groups disables standard exclusions.</p>
     *
     * @param groups the exclusion groups
     * @return this builder
     */
    public NamePatternBuilder standardExcludes(
            StandardExcludes.ExcludeGroup... groups) {
        List<StandardExcludes.ExcludeGroup> policies = List.of(groups);

        for (StandardExcludes.ExcludeGroup policy : policies) {
            if (!StandardExcludes.PLEXUS.equals(policy)) {
                throw new IllegalArgumentException(
                        "Unsupported standard exclusion group: " + policy);
            }
        }

        standardExcludes = policies;
        return this;
    }

    /**
     * Sets the case-sensitivity policy.
     *
     * @param policy the case-sensitivity policy
     * @return this builder
     */
    public NamePatternBuilder caseSensitive(CaseSensitivity policy) {
        caseSensitivity = Objects.requireNonNull(policy, "caseSensitivity");
        return this;
    }

    /**
     * Creates a matcher for paths and archive entry names.
     *
     * <p>Subsequent builder changes do not affect the returned matcher.
     * Platform-default case sensitivity is resolved when this method runs.</p>
     *
     * @return a matcher using the current name-selection rules
     */
    public NameMatcher toMatcher() {
        NameSelector selector = new NameSelector();
        selector.setIncludes(includes.clone());
        selector.setExcludes(excludes.clone());
        selector.setCaseSensitive(CaseSensitivities.resolve(caseSensitivity));
        selector.setUseDefaultExcludes(
                standardExcludes.contains(StandardExcludes.PLEXUS));

        return new NameMatcher() {
            @Override
            public boolean matches(Path path) {
                return selector.matchesName(
                        Objects.requireNonNull(path, "path").toString());
            }

            @Override
            public boolean matches(ArchiveEntryInfo entry) {
                return selector.matchesName(
                        Objects.requireNonNull(entry, "entry").name());
            }
        };
    }

    private static String[] copyPatterns(String[] patterns) {
        return List.of(patterns).toArray(String[]::new);
    }

    /**
     * Exposes legacy selection by name without constructing entry metadata.
     */
    private static final class NameSelector extends IncludeExcludeFileSelector {

        boolean matchesName(String name) {
            // Mirrors the superclass's private name canonicalization.
            String canonicalName = Objects.requireNonNull(name, "name")
                    .replace('/', File.separatorChar)
                    .replace('\\', File.separatorChar);

            return isIncluded(canonicalName) && !isExcluded(canonicalName);
        }
    }
}