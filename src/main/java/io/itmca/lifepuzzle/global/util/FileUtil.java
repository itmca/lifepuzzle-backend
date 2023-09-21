package io.itmca.lifepuzzle.global.util;

import static io.itmca.lifepuzzle.global.util.StreamUtil.toStream;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

  public static Boolean isExistFolder(String folderPath) {
    var folder = new File(folderPath);
    return folder.exists();
  }

  public static void createAllFolder(String folderPath) {
    var folder = new File(folderPath);
    folder.mkdirs();
  }

  public static String getNormalizedFileName(MultipartFile file) {
    return URLDecoder.decode(file.getOriginalFilename(), StandardCharsets.UTF_8);
  }

  public static File saveMultiPartFileInLocal(byte[] bytes, String fileURL)
      throws IOException {
    return Files.write(Path.of(fileURL), bytes).toFile();
  }

  public static Stream<List<MultipartFile>> getGroupByFileName(List<MultipartFile> files) {
    return toStream(files)
        .collect(Collectors.groupingBy(file -> file.getOriginalFilename()))
        .values()
        .stream();
  }
}
