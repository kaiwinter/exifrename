package com.github.kaiwinter.exifrename;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

import com.drew.imaging.ImageProcessingException;
import com.github.kaiwinter.exifrename.type.RenameOperation;
import com.github.kaiwinter.exifrename.uc.RenameUc;

public class Main {

   private static final boolean DO_RENAME = false;

   public static void main(String[] args) throws ImageProcessingException, IOException {

      RenameUc renameUc = new RenameUc();
      Set<RenameOperation> createRenamePreviewResult = renameUc.createRenameOperations(Paths.get("C:/temp/test"));

      if (DO_RENAME) {
         renameUc.executeRenameOperations(createRenamePreviewResult);
      }
   }
}
