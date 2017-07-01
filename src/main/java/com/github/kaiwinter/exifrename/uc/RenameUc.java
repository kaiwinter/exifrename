package com.github.kaiwinter.exifrename.uc;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageProcessingException;

/**
 * Use case to run the rename action for a complete directory.
 */
public class RenameUc {

   private static final Logger LOGGER = LoggerFactory.getLogger(RenameUc.class.getSimpleName());

   /**
    * Starts the rename action for the passed directory. The result will be an object which contains a preview of the
    * rename operations. The renaming has to be executed afterwards by the result object.
    *
    * @see RenamePreviewResult#doRename()
    *
    * @param directory
    *           the directory which contains the images to rename
    * @return {@link RenamePreviewResult} which contains the renaming operations. The actual renaming has to be started
    *         by a call on this object
    * @throws IOException
    *            if an I/O error occurs
    * @throws ImageProcessingException
    *            for general processing errors
    */
   public RenamePreviewResult createRenamePreviewResult(Path directory) throws IOException, ImageProcessingException {
      RenamePreviewResult renamePreviewResult = new RenamePreviewResult();
      if (!Files.isDirectory(directory)) {
         LOGGER.error("'{}' is not a directory", directory);
         return renamePreviewResult;
      }
      DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(directory);

      Iterator<Path> iterator = newDirectoryStream.iterator();
      while (iterator.hasNext()) {
         Path filepath = iterator.next();
         renamePreviewResult.processFile(filepath);
      }

      return renamePreviewResult;
   }

}
