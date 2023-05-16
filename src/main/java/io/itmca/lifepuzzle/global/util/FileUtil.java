package io.itmca.lifepuzzle.global.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
  public static String getBaseFolderPath() {
    var now = LocalDateTime.now();
    return String.format("life-puzzle/%s/%s/%s", now.getYear(), now.getMonth().getValue(),
        now.getDayOfMonth());
  }

  public static Boolean isExistFolder(String folderPath) {
    var folder = new File(folderPath);
    return folder.exists();
  }

  public static void createAllFolder(String folderPath) {
    var folder = new File(folderPath);
    folder.mkdirs();
  }

  public static List<String> getFilePaths(List<MultipartFile> multipartFiles) {
    return multipartFiles.stream()
        .map(file -> addRandomValueFilePrefix(file.getOriginalFilename()))
        .toList();
  }

  public static String addRandomValueFilePrefix(String fileName) {
    return String.format("%d_%s", Math.round(Math.random() * 1000000), fileName);
  }

  public static Boolean isMultiPartFile(MultipartFile multipartFile) {
    return multipartFile != null && !multipartFile.isEmpty();
  }

  public static File saveMultiPartFileInLocal(MultipartFile multipartFile, String fileURL)
      throws IOException {
    var file = new File(fileURL);

    multipartFile.transferTo(file);

    return file;
  }

  public static void deleteLocalFile(File file) {
    file.delete();
  }
}
