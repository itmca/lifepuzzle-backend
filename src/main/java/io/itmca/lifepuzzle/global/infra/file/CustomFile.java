package io.itmca.lifepuzzle.global.infra.file;

import org.springframework.stereotype.Service;

@Service
public interface CustomFile {
  CustomFile resize();

  String getBase();

  String getFileName();

  byte[] getBytes();

}