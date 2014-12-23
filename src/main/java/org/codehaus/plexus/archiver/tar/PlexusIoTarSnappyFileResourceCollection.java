package org.codehaus.plexus.archiver.tar;

import java.io.File;

public class PlexusIoTarSnappyFileResourceCollection
    extends PlexusIoTarFileResourceCollection
{
    
    protected TarFile newTarFile( File file )
    {
        return new SnappyTarFile( file );
    }
}
