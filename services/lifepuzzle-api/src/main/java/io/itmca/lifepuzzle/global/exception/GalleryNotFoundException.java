package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;

public class GalleryNotFoundException extends NotFoundException {
  public GalleryNotFoundException(Long heroId) {
    super(String.format("Gallery is not found - heroId: %s", heroId));
  }
}
