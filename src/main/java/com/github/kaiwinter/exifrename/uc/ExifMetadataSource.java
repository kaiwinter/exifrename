package com.github.kaiwinter.exifrename.uc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifSubIFDDirectory;

/**
 * Accesses image files to load exif meta data.
 */
public final class ExifMetadataSource {

   private static final Logger LOGGER = LoggerFactory.getLogger(ExifMetadataSource.class.getSimpleName());

   /**
    * Reads the exif tag for the original date/time. If more than one exif tags exist the first one is used.
    *
    * @param filepath
    *           the {@link Path} to the file, not <code>null</code>
    * @return The date/time original.
    * @throws ImageProcessingException
    *            for general processing errors
    * @throws IOException
    *            on errors while accessing the file
    */
   public Date getDateTimeOriginal(Path filepath) throws ImageProcessingException, IOException {
      Objects.requireNonNull(filepath);
      Metadata metadata = ImageMetadataReader.readMetadata(filepath.toFile());
      Collection<ExifSubIFDDirectory> directoriesOfType = metadata.getDirectoriesOfType(ExifSubIFDDirectory.class);

      if (directoriesOfType.size() > 1) {
         LOGGER.error("More than one ExifIFD0Directory");
      }

      ExifSubIFDDirectory exifSubIFDDirectory = directoriesOfType.iterator().next();
      Date date = exifSubIFDDirectory.getDate(ExifDirectoryBase.TAG_DATETIME_ORIGINAL, TimeZone.getDefault());
      return date;
   }
}
