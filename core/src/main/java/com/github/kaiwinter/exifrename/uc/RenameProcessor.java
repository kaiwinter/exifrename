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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageProcessingException;
import com.github.kaiwinter.exifrename.type.NewFilename;
import com.github.kaiwinter.exifrename.type.OldFilename;
import com.github.kaiwinter.exifrename.type.RenameOperation;

/**
 * Processes files and stores the renaming operations as a preview. After the preview is generated the renaming
 * operations can be executed.
 * <p>
 * Rename operations are stored in a Map which maps a {@link NewFilename} to a List of {@link OldFilename}s. This is
 * necessary because two files could result in having the same {@link NewFilename}. In this case a number is added to
 * the {@link NewFilename} when the rename operation is executed.
 * </p>
 */
public final class RenameProcessor {

   private static final Logger LOGGER = LoggerFactory.getLogger(RenameProcessor.class.getSimpleName());

   private final Map<NewFilename, List<OldFilename>> newFilename2oldFilename = new HashMap<>();

   private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

   /**
    * Adds the file to the internal Map.
    *
    * @param filepath
    *           the {@link Path} to the file, not <code>null</code>
    * @throws ImageProcessingException
    *            for general processing errors
    * @throws IOException
    *            on errors while accessing the file
    */
   public void addFile(Path filepath) throws ImageProcessingException, IOException {
      Objects.requireNonNull(filepath);
      if (!Files.isRegularFile(filepath)) {
         LOGGER.warn("'{}' is not a file (skipping)", filepath.getFileName());
      }
      ExifMetadataSource metadataExtractor = new ExifMetadataSource();
      Date date = metadataExtractor.getDateTimeOriginal(filepath);
      if (date == null) {
         LOGGER.warn("{}: No date set for image", filepath.getFileName());
         return;
      }

      OldFilename oldFilename = OldFilename.of(filepath);
      String formattedDate = simpleDateFormat.format(date);

      String extension = oldFilename.getExtension();
      Path newFilenamePath = Paths.get(oldFilename.getPath().getParent().toString(), formattedDate + "." + extension);
      NewFilename newFilename = NewFilename.of(newFilenamePath, date);
      LOGGER.debug("Parsed date/time from file '{}': {}, new filename: '{}'", filepath, simpleDateFormat.format(date),
         newFilename);

      List<OldFilename> oldFilenames = newFilename2oldFilename.get(newFilename);
      if (oldFilenames == null) {
         oldFilenames = new ArrayList<>();
         newFilename2oldFilename.put(newFilename, oldFilenames);
      } else {
         LOGGER.debug("Collision in new Filename: {}", newFilename);
      }
      oldFilenames.add(oldFilename);
   }

   /**
    * Creates the {@link RenameOperation}s from the processed files.
    *
    * @return the {@link RenameOperation}s
    */
   public Set<RenameOperation> createRenameOperations() {
      Set<RenameOperation> renameOperations = new HashSet<>();

      for (Entry<NewFilename, List<OldFilename>> entrySet : newFilename2oldFilename.entrySet()) {
         NewFilename newFilename = entrySet.getKey();
         List<OldFilename> oldFilenames = entrySet.getValue();

         if (oldFilenames.size() == 1) {
            Path oldFilename = oldFilenames.get(0).getPath();
            Path newFilenamePath = newFilename.getPath();
            renameOperations.add(new RenameOperation(oldFilename, newFilenamePath, newFilename.getExifOriginalDate()));
         } else {
            int fileNumber = 1;
            for (OldFilename oldFilename : oldFilenames) {
               NewFilename newFilenameWithNumber = newFilename.createNewFilenameWithNumber(fileNumber++);
               renameOperations.add(new RenameOperation(oldFilename.getPath(), newFilenameWithNumber.getPath(),
                  newFilename.getExifOriginalDate()));
            }
         }
      }

      return renameOperations;
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
