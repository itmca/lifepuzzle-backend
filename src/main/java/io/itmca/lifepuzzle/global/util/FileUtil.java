package io.itmca.lifepuzzle.global.util;

import static io.itmca.lifepuzzle.global.util.StreamUtil.toStreamOrEmptyStream;
import static java.util.stream.Collectors.groupingBy;

import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
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

  public static <R extends CustomFile> List<R> handleSameNameContents(
      List<MultipartFile> files,
      Function<MultipartFile, R> normalCaseHandler,
      BiFunction<MultipartFile, String, R> duplicateHandler) {
    return toStreamOrEmptyStream(files)
        .collect(groupingBy(file -> file.getOriginalFilename()))
        .values()
        .stream()
        .flatMap(sameNameContents -> {
          if (sameNameContents.size() <= 1) {
            return sameNameContents.stream()
                .map(normalCaseHandler::apply);
          }

          var postfix = new AtomicInteger();

          return sameNameContents.stream()
              .map(c -> duplicateHandler.apply(c, String.valueOf(postfix.getAndAdd(1))));
        })
        .toList();
  }
}
