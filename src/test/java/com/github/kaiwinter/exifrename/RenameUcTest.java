package com.github.kaiwinter.exifrename;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.drew.imaging.ImageProcessingException;
import com.github.kaiwinter.exifrename.type.NewFilename;
import com.github.kaiwinter.exifrename.type.OldFilename;
import com.github.kaiwinter.exifrename.uc.RenamePreviewResult;
import com.github.kaiwinter.exifrename.uc.RenameUc;

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
      RenamePreviewResult createRenamePreviewResult = renameUc.createRenamePreviewResult(path);

      Map<NewFilename, List<OldFilename>> newFilename2oldFilename = createRenamePreviewResult
         .getNewFilename2oldFilename();

      assertEquals(2, newFilename2oldFilename.size());

      List<OldFilename> list = newFilename2oldFilename.get(NewFilename.of("20170701_130000.jpg"));
      assertEquals(1, list.size());
      assertEquals("image_1.jpg", list.iterator().next().getPath().getFileName().toString());

      list = newFilename2oldFilename.get(NewFilename.of("20170701_140000.jpg"));
      assertEquals(1, list.size());
      assertEquals("image_2.jpg", list.iterator().next().getPath().getFileName().toString());
   }

   /**
    * The directory to process contains two images which both have the same date/time original.
    */
   @Test
   public void sameDateTimeOriginal() throws ImageProcessingException, IOException, URISyntaxException {

      RenameUc renameUc = new RenameUc();
      URL resource = RenameUcTest.class.getResource("/same_datetime_original");
      Path path = Paths.get(resource.toURI());
      RenamePreviewResult createRenamePreviewResult = renameUc.createRenamePreviewResult(path);

      Map<NewFilename, List<OldFilename>> newFilename2oldFilename = createRenamePreviewResult
         .getNewFilename2oldFilename();

      assertEquals(1, newFilename2oldFilename.size());
      assertEquals(2, newFilename2oldFilename.get(NewFilename.of("20170701_130000.jpg")).size());
   }
}
