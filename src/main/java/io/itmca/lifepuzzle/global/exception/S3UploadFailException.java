package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.ServerExecutionFailException;

public class S3UploadFailException extends ServerExecutionFailException {
  public S3UploadFailException(String fileName, Throwable cause) {
    super("S3 Upload Fail - fileName: " + fileName, cause);
  }
}
