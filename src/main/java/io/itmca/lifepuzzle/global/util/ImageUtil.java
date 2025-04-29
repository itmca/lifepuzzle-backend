package io.itmca.lifepuzzle.global.util;

import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.ImageFormats;
import io.github.techgnious.exception.ImageException;
import io.itmca.lifepuzzle.global.model.FileDimension;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtil {
  public static IVCompressor resizer = new IVCompressor();

  public static byte[] getResizeImage(byte[] image, String contentType, int targetWidth,
                                      int targetHeight) throws ImageException {
    var customRes = new IVSize();
    customRes.setWidth(targetWidth);
    customRes.setHeight(targetHeight);

    var ext = contentType.split("/").length > 1 ? contentType.split("/")[1] : "jpeg";
    return resizer.resizeImageWithCustomRes(image, ImageFormats.valueOf(ext.toUpperCase()),
        customRes);
  }

  public static boolean isResizable(byte[] image, int targetWidth, int targetHeight) {
    try {
      var imageInfo = ImageIO.read(new ByteArrayInputStream(image));
      var validateWidth = imageInfo.getWidth() > targetWidth;
      var validateHeight = imageInfo.getHeight() > targetHeight;

      return validateWidth && validateHeight;
    } catch (IOException ex) {
      return false;
    }
  }

  public static FileDimension getResizeFileDimension(byte[] image, int baseWidth)
      throws IOException {
    var bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
    var imageHeight = bufferedImage.getHeight();
    var imageWidth = bufferedImage.getWidth();

    var ratio = (float) imageWidth / imageHeight;
    var shouldResizeByWidth = baseWidth < imageWidth;

    return shouldResizeByWidth
        ? new FileDimension(baseWidth, (int) (baseWidth / ratio))
        : new FileDimension(imageWidth, imageHeight);
  }
}
