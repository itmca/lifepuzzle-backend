package io.itmca.lifepuzzle.global.file;

import java.util.Optional;

public interface Resizable<T extends CustomFile> {
  Optional<T> resize();

  Optional<T> resize(int fixedWidth);
}
