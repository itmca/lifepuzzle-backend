package io.itmca.lifepuzzle.global.infra.file;

public interface Resizable<T extends CustomFile> {
  T resize();
}
