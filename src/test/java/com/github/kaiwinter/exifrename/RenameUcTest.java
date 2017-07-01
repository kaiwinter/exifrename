package com.github.kaiwinter.exifrename;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.drew.imaging.ImageProcessingException;
import com.github.kaiwinter.exifrename.type.RenameOperation;
import com.github.kaiwinter.exifrename.uc.RenameUc;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

/**
 * Tests for {@link RenameUc}.
 */
public class RenameUcTest {

   /**
    * The directory contains two images which have different date/time originals.
    */
   @Test
   public void twoDifferentImages() throws ImageProcessingException, IOException, URISyntaxException {

      RenameUc renameUc = new RenameUc();
      URL resource = RenameUcTest.class.getResource("/two_different_images");
      Path path = Paths.get(resource.toURI());
      Set<RenameOperation> renameOperations = renameUc.createRenameOperations(path);

      assertEquals(2, renameOperations.size());

      assertTrue(renameOperations.stream().anyMatch(item -> {
         Path oldName = Paths.get(path.toString(), "image_1.jpg");
         Path expectedNewName = Paths.get(path.toString(), "20170701_130000.jpg");
         return (oldName).equals(item.getOldFilename()) && (expectedNewName).equals(item.getNewFilenamePath());
      }));

      assertTrue(renameOperations.stream().anyMatch(item -> {
         Path oldName = Paths.get(path.toString(), "image_2.jpg");
         Path expectedNewName = Paths.get(path.toString(), "20170701_140000.jpg");
         return (oldName).equals(item.getOldFilename()) && (expectedNewName).equals(item.getNewFilenamePath());
      }));
   }

   /**
    * The directory to process contains two images which both have the same date/time original.
    */
   @Test
   public void sameDateTimeOriginal() throws ImageProcessingException, IOException, URISyntaxException {
      RenameUc renameUc = new RenameUc();
      URL resource = RenameUcTest.class.getResource("/same_datetime_original");
      Path path = Paths.get(resource.toURI());
      Set<RenameOperation> renameOperations = renameUc.createRenameOperations(path);

      assertTrue(renameOperations.stream().anyMatch(item -> {
         Path oldName = Paths.get(path.toString(), "image_1.jpg");
         Path expectedNewName = Paths.get(path.toString(), "20170701_130000_1.jpg");
         return (oldName).equals(item.getOldFilename()) && (expectedNewName).equals(item.getNewFilenamePath());
      }));

      assertTrue(renameOperations.stream().anyMatch(item -> {
         Path oldName = Paths.get(path.toString(), "image_2.jpg");
         Path expectedNewName = Paths.get(path.toString(), "20170701_130000_2.jpg");
         return (oldName).equals(item.getOldFilename()) && (expectedNewName).equals(item.getNewFilenamePath());
      }));
   }

   /**
    * Uses an in-memory file system to test the rename. The directory contains one file which should be renamed
    * according to the {@link RenameOperation}.
    */
   @Test
   public void executeRenameOperations() throws IOException {
      FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
      Path foo = fs.getPath("/dir");
      Files.createDirectory(foo);

      Path hello = foo.resolve("IMG_12345.jpg");
      Files.write(hello, "".getBytes());

      Set<RenameOperation> renameOperations = new HashSet<>();
      renameOperations
         .add(new RenameOperation(fs.getPath("/dir", "IMG_12345.jpg"), fs.getPath("/dir", "20160701_2336.jpg")));

      RenameUc renameUc = new RenameUc();
      renameUc.executeRenameOperations(renameOperations);

      DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(foo);
      assertEquals(fs.getPath("/dir", "20160701_2336.jpg"), newDirectoryStream.iterator().next());
   }

}
