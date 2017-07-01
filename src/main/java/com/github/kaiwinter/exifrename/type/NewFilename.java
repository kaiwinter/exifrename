package com.github.kaiwinter.exifrename.type;

import java.util.Objects;

/**
 * The new file name of a file to rename. This class exists to not get confused by using Strings for the new and old
 * file name.
 */
public final class NewFilename {
   private final String name;

   /**
    * Constructs a new {@link NewFilename}.
    *
    * @param name
    *           the name of the new file.
    */
   public NewFilename(String name) {
      Objects.requireNonNull(name);
      this.name = name;
   }

   /**
    * Factory method for creating an instance in a fluent way.
    *
    * @param name
    *           the name of the new file
    * @return a {@link NewFilename} object
    */
   public static NewFilename of(String name) {
      return new NewFilename(name);
   }

   /**
    * @return the name of the new file
    */
   public String getName() {
      return name;
   }

   @Override
   public String toString() {
      return name.toString();
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
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
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      return true;
   }
}