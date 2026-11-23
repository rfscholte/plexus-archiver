package org.codehaus.plexus.archivers.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.codehaus.plexus.archiver.util.AbstractFileSet;
import org.codehaus.plexus.components.io.filemappers.FileMapper;
import org.codehaus.plexus.components.io.functions.InputStreamTransformer;

final class DefaultResourceEmitSettings implements ResourceEmitSettings {

	private Optional<String> prefix = Optional.empty();
	private Optional<EmptyDirectoryHandling> emptyDirectoryHandling = Optional.empty();
	private Optional<FileMapper[]> fileMappers = Optional.empty();
	private Optional<InputStreamTransformer> streamTransformer = Optional.empty();

	@Override
	public void setPrefix(String prefix) {
		this.prefix = Optional.of(prefix);
	}
	
    @Override
    public void setEmptyDirectoryHandling(EmptyDirectoryHandling handling) {
    	this.emptyDirectoryHandling = Optional.of(handling);
    }

    @Override
	public void setFileMappers(FileMapper... fileMappers) {
    	this.fileMappers = Optional.of(Arrays.copyOf(fileMappers, fileMappers.length));
    }

    @Override
    public void setStreamTransformer(InputStreamTransformer transformer) {
    	this.streamTransformer = Optional.of(transformer);
    }
    
    void apply(AbstractFileSet<?> fileSet) {
    	prefix.ifPresent(fileSet::setPrefix);
    	emptyDirectoryHandling.map(EmptyDirectoryHandling.INCLUDE::equals).ifPresent(fileSet::setIncludingEmptyDirectories);
    	fileMappers.map(m-> Arrays.copyOf(m, m.length)).ifPresent(fileSet::setFileMappers);
    	streamTransformer.ifPresent(fileSet::setStreamTransformer);
    }
}
