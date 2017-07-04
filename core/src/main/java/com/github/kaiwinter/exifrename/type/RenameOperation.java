package com.github.kaiwinter.exifrename.type;

import java.nio.file.Path;

public final class RenameOperation {

   private final Path oldFilename;
   private final Path newFilenamePath;

   public RenameOperation(Path oldFilename, Path newFilenamePath) {
      this.oldFilename = oldFilename;
      this.newFilenamePath = newFilenamePath;
   }

   public Path getOldFilename() {
      return oldFilename;
   }

   public Path getNewFilenamePath() {
      return newFilenamePath;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((newFilenamePath == null) ? 0 : newFilenamePath.hashCode());
      result = prime * result + ((oldFilename == null) ? 0 : oldFilename.hashCode());
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
      RenameOperation other = (RenameOperation) obj;
      if (newFilenamePath == null) {
         if (other.newFilenamePath != null)
            return false;
      } else if (!newFilenamePath.equals(other.newFilenamePath))
         return false;
      if (oldFilename == null) {
         if (other.oldFilename != null)
            return false;
      } else if (!oldFilename.equals(other.oldFilename))
         return false;
      return true;
   }

}
