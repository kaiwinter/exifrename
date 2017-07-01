package com.github.kaiwinter.exifrename.uc;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageProcessingException;
import com.github.kaiwinter.exifrename.type.RenameOperation;

/**
 * Use case to run the rename action for a complete directory.
 */
public final class RenameUc {

   private static final Logger LOGGER = LoggerFactory.getLogger(RenameUc.class.getSimpleName());

   /**
    * Starts the rename action for the passed directory. The result will be an object which contains a preview of the
    * rename operations. The renaming has to be executed afterwards by the result object.
    *
    * @see RenameProcessor#doRename()
    *
    * @param directory
    *           the directory which contains the images to rename
    * @return the {@link RenameOperation}s
    * @throws IOException
    *            if an I/O error occurs
    * @throws ImageProcessingException
    *            for general processing errors
    */
   public Set<RenameOperation> createRenameOperations(Path directory) throws IOException, ImageProcessingException {
      RenameProcessor renameProcessor = new RenameProcessor();
      if (!Files.isDirectory(directory)) {
         LOGGER.error("'{}' is not a directory", directory);
         return Collections.emptySet();
      }
      DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(directory);

      Iterator<Path> iterator = newDirectoryStream.iterator();
      while (iterator.hasNext()) {
         Path filepath = iterator.next();
         renameProcessor.addFile(filepath);
      }

      Set<RenameOperation> createRenameOperations = renameProcessor.createRenameOperations();

      return createRenameOperations;
   }

   public void executeRenameOperations(Set<RenameOperation> renameOperations) throws IOException {
      for (RenameOperation renameOperation : renameOperations) {
         Path oldFilename = renameOperation.getOldFilename();
         Path newFilenamePath = renameOperation.getNewFilenamePath();
         LOGGER.info("Rename '{}' to '{}'", oldFilename, newFilenamePath);
         Files.move(oldFilename, newFilenamePath);
      }
   }

}
