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

import java.nio.file.attribute.FileTime;
import java.util.Comparator;
import java.util.Optional;

import org.codehaus.plexus.archiver.Archiver;

/**
 * Applies the configuration to the applied archiver
 * 
 * @since 5.0.0
 */
final class DefaultReproducibleBuildSettings implements ReproducibleBuildSettings {

	private Optional<Comparator<String>> filenameComparator = Optional.empty();
	private Optional<Integer> overrideGid = Optional.empty();
	private Optional<String> overrideGroupName = Optional.empty();
	private Optional<FileTime> overrideLastModifiedTime = Optional.empty();
	private Optional<Integer> overrideUid = Optional.empty();
	private Optional<String> overrideUserName = Optional.empty();
	
	@Override
	public void setFilenameComparator(Comparator<String> filenameComparator) {
		this.filenameComparator = Optional.of(filenameComparator);
	}
	
	@Override
	public void setOverrideGid(int gid) {
		this.overrideGid = Optional.of(gid);
	}

	@Override
	public void setOverrideLastModifiedTime(FileTime fileTime) {
		this.overrideLastModifiedTime = Optional.of(fileTime);
	}

	@Override
	public void setOverrideUid(int uid) {
		this.overrideUid = Optional.of(uid);
	}

	@Override
	public void setOverrideUserName(String userName) {
		this.overrideUserName = Optional.of(userName);
	}


	@Override
	public void setOverrideGroupName(String groupName) {
		this.overrideGroupName = Optional.of(groupName);
	}

	void apply(Archiver archiver) {
		filenameComparator.ifPresent(archiver::setFilenameComparator);
		overrideGid.ifPresent(archiver::setOverrideGid);
		overrideGroupName.ifPresent(archiver::setOverrideGroupName);
		overrideLastModifiedTime.ifPresent(archiver::configureReproducibleBuild);
		overrideUid.ifPresent(archiver::setOverrideUid);
		overrideUserName.ifPresent(archiver::setOverrideUserName);
	}
}
