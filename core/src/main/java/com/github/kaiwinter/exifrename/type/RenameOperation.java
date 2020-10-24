package com.github.kaiwinter.exifrename.type;

import java.nio.file.Path;
import java.util.Date;

public final class RenameOperation {

   private final Path oldFilename;
   private final Path newFilenamePath;
   private final Date exifOriginalDate;

   public RenameOperation(Path oldFilename, Path newFilenamePath, Date exifOriginalDate) {
      this.oldFilename = oldFilename;
      this.newFilenamePath = newFilenamePath;
      this.exifOriginalDate = exifOriginalDate;
   }

   public Path getOldFilename() {
      return oldFilename;
   }

   public Path getNewFilenamePath() {
      return newFilenamePath;
   }

   public Date getExifOriginalDate() {
      return exifOriginalDate;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((exifOriginalDate == null) ? 0 : exifOriginalDate.hashCode());
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
      if (exifOriginalDate == null) {
         if (other.exifOriginalDate != null)
            return false;
      } else if (!exifOriginalDate.equals(other.exifOriginalDate))
         return false;
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

   @Override
   public String toString() {
      return "RenameOperation [oldFilename=" + oldFilename + ", newFilenamePath=" + newFilenamePath + ", exifOriginalDate=" + exifOriginalDate + "]";
   }
}
