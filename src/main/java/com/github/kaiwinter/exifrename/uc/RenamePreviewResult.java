package com.github.kaiwinter.exifrename.uc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageProcessingException;
import com.github.kaiwinter.exifrename.type.NewFilename;
import com.github.kaiwinter.exifrename.type.OldFilename;

/**
 * Processes files and stores the renaming operations as a preview. After the preview is generated the renaming
 * operations can be executed.
 * <p>
 * Rename operations are stored in a Map which maps a {@link NewFilename} to a List of {@link OldFilename}s. This is
 * necessary because two files could result in having the same {@link NewFilename}. In this case a number is added to
 * the {@link NewFilename} when the rename operation is executed.
 * </p>
 */
public final class RenamePreviewResult {

   private static final Logger LOGGER = LoggerFactory.getLogger(RenamePreviewResult.class.getSimpleName());

   private final Map<NewFilename, List<OldFilename>> newFilename2oldFilename = new HashMap<>();

   private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

   /**
    * Processes the passed file and adds the rename operation to the internal Map. If two files will have the same new
    * filename, a number will be added to the filename.
    *
    * @param filepath
    *           the {@link Path} to the file, not <code>null</code>
    * @throws ImageProcessingException
    *            for general processing errors
    * @throws IOException
    *            on errors while accessing the file
    */
   public void processFile(Path filepath) throws ImageProcessingException, IOException {
      Objects.requireNonNull(filepath);
      if (!Files.isRegularFile(filepath)) {
         LOGGER.warn("'{}' is not a file (skipping)", filepath);
      }
      ExifMetadataSource metadataExtractor = new ExifMetadataSource();
      Date date = metadataExtractor.getDateTimeOriginal(filepath);
      if (date == null) {
         LOGGER.warn("No date set for image '{}' (ignoring image)", filepath);
         return;
      }

      String formattedDate = simpleDateFormat.format(date);

      int lastIndexOf = filepath.getFileName().toString().lastIndexOf('.');
      String extension = filepath.getFileName().toString().substring(lastIndexOf + 1);
      NewFilename newFilename = NewFilename.of(formattedDate + "." + extension);
      LOGGER.debug("Parsed date/time from file '{}': {}, new filename: '{}'", filepath, simpleDateFormat.format(date),
         newFilename);

      List<OldFilename> oldFilenames = newFilename2oldFilename.get(newFilename);
      if (oldFilenames == null) {
         oldFilenames = new ArrayList<>();
         newFilename2oldFilename.put(newFilename, oldFilenames);
      } else {
         LOGGER.debug("Collision in new Filename: {}", newFilename);
      }
      oldFilenames.add(OldFilename.of(filepath));
   }

   /**
    * Executes the rename operations from the internal Map.
    *
    * @throws IOException
    *            if an I/O error occurs
    */
   public void doRename() throws IOException {
      for (Entry<NewFilename, List<OldFilename>> entrySet : newFilename2oldFilename.entrySet()) {
         NewFilename newFilename = entrySet.getKey();
         List<OldFilename> oldFilenames = entrySet.getValue();

         if (oldFilenames.size() == 1) {
            Path oldFilename = oldFilenames.get(0).getPath();
            Path newFilenamePath = Paths.get(oldFilename.getRoot().toString(), newFilename.getName());
            LOGGER.info("Rename '{}' to '{}'", oldFilename, newFilenamePath);
            Files.move(oldFilename, newFilenamePath);
         } else {
            int fileNumber = 1;
            for (OldFilename oldFilename : oldFilenames) {
               int lastIndexOf = newFilename.getName().lastIndexOf('.');
               String dotWithExtension = newFilename.getName().substring(lastIndexOf);
               String newFilenameWithNumber = newFilename.getName().substring(0, lastIndexOf) + "_" + fileNumber++
                  + dotWithExtension;
               Path newFilenamePath = Paths.get(oldFilename.getPath().getRoot().toString(), newFilenameWithNumber);
               LOGGER.info("Rename '{}' to '{}'", oldFilename, newFilenamePath);
               Files.move(oldFilename.getPath(), newFilenamePath);
            }
         }
      }
   }

   /**
    * Returns the rename operations.
    *
    * @return the rename operations
    */
   public Map<NewFilename, List<OldFilename>> getNewFilename2oldFilename() {
      return Collections.unmodifiableMap(newFilename2oldFilename);
   }
}
