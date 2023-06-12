package io.itmca.lifepuzzle.global.infra.file.repository;

import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import java.io.IOException;

public interface FileRepository {
  public void upload(CustomFile customFile, String path) throws IOException;
}
