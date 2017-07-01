package com.github.kaiwinter.exifrename;

import java.io.IOException;
import java.nio.file.Paths;

import com.drew.imaging.ImageProcessingException;
import com.github.kaiwinter.exifrename.uc.RenamePreviewResult;
import com.github.kaiwinter.exifrename.uc.RenameUc;

public class Main {

   private static final boolean DO_RENAME = false;

   public static void main(String[] args) throws ImageProcessingException, IOException {

      RenameUc renameUc = new RenameUc();
      RenamePreviewResult createRenamePreviewResult = renameUc.createRenamePreviewResult(Paths.get("C:/temp/test"));

      if (DO_RENAME) {
         createRenamePreviewResult.doRename();
      }
   }
}
