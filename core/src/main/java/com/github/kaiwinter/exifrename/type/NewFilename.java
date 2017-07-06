package com.github.kaiwinter.exifrename.type;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

/**
 * The new file name of a file to rename. This class exists to not get confused by using Strings for the new and old
 * file name.
 */
public final class NewFilename {

   private final Path path;
   private final Date exifOriginalDate;

   /**
    * Constructs a new {@link NewFilename}.
    *
    * @param path
    *           the path of the new file.
    * @param exifOriginalDate
    */
   private NewFilename(Path path, Date exifOriginalDate) {
      this.exifOriginalDate = exifOriginalDate;
      Objects.requireNonNull(path);
      this.path = path;
   }

   /**
    * Factory method for creating an instance in a fluent way.
    *
    * @param name
    *           the name of the new file
    * @param exifOriginalDate
    *           the exif original date
    * @return a {@link NewFilename} object
    */
   public static NewFilename of(Path name, Date exifOriginalDate) {
      return new NewFilename(name, exifOriginalDate);
   }

   /**
    * @return the path of the new file
    */
   public Path getPath() {
      return path;
   }

   public Date getExifOriginalDate() {
      return exifOriginalDate;
   }

   @Override
   public String toString() {
      return path.toString();
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((path == null) ? 0 : path.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      NewFilename other = (NewFilename) obj;
      if (path == null) {
         if (other.path != null)
            return false;
      } else if (!path.equals(other.path))
         return false;
      return true;
   }

   public NewFilename createNewFilenameWithNumber(int fileNumber) {
      int lastIndexOf = path.toString().lastIndexOf('.');
      String dotWithExtension = path.toString().substring(lastIndexOf);
      String newFilenameWithNumber = path.toString().substring(0, lastIndexOf) + "_" + fileNumber++ + dotWithExtension;

      return NewFilename.of(Paths.get(newFilenameWithNumber), exifOriginalDate);
   }
}