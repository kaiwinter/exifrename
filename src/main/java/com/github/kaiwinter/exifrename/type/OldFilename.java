package com.github.kaiwinter.exifrename.type;

import java.nio.file.Path;
import java.util.Objects;

/**
 * The old file name of a file to rename. This class exists to not get confused by using Strings for the new and old
 * file name.
 */
public final class OldFilename {

   private final Path path;

   /**
    * Constructs a new {@link OldFilename}.
    *
    * @param path
    *           the path to the old file
    */
   private OldFilename(Path path) {
      Objects.requireNonNull(path);
      this.path = path;
   }

   /**
    * Factory method for creating an instance in a fluent way.
    *
    * @param filepath
    *           the filepath of the old file
    * @return a {@link OldFilename} object
    */
   public static OldFilename of(Path filepath) {
      return new OldFilename(filepath);
   }

   /**
    * @return the {@link Path} to the old file
    */
   public Path getPath() {
      return path;
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
      OldFilename other = (OldFilename) obj;
      if (path == null) {
         if (other.path != null)
            return false;
      } else if (!path.equals(other.path))
         return false;
      return true;
   }
}