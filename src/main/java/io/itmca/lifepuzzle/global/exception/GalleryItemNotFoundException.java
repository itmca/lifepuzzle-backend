package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;

public class GalleryItemNotFoundException extends NotFoundException {
  private GalleryItemNotFoundException(String message) {
    super(message);
  }

  public static GalleryItemNotFoundException of(Long galleryId) {
    return new GalleryItemNotFoundException(String.format("Gallery item not found: %s", galleryId));
  }
}
