package io.itmca.lifepuzzle.global.file.repository;

import io.itmca.lifepuzzle.global.file.CustomFile;
import java.io.IOException;

public interface FileRepository {
  void upload(CustomFile customFile) throws IOException;
}
