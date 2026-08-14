package org.codehaus.plexus.archiver.manager;

import javax.annotation.Nonnull;

import java.io.File;
import java.util.Locale;

import org.codehaus.plexus.util.StringUtils;

class ArchiverManagerUtils {

    private ArchiverManagerUtils() {}

    static @Nonnull String getFileExtension(@Nonnull File file) {

        String fileName = file.getName().toLowerCase(Locale.ROOT);
        String[] tokens = StringUtils.split(fileName, ".");

        String archiveExt = "";

        if (tokens.length == 2) {
            archiveExt = tokens[1];
        } else if (tokens.length > 2 && "tar".equals(tokens[tokens.length - 2])) {
            archiveExt = "tar." + tokens[tokens.length - 1];
        } else if (tokens.length > 2) {
            archiveExt = tokens[tokens.length - 1];
        }

        return archiveExt;
    }
}
