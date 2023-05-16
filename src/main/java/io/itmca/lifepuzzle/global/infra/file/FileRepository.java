package io.itmca.lifepuzzle.global.infra.file;

import java.io.File;

public interface FileRepository {
  public String upload(File file, String saveURL);
}
