package io.itmca.lifepuzzle.domain.auth;

import java.util.List;
import lombok.Getter;

@Getter
public class FacebookPhoto {
  private List<FacebookImage> images;
  private String id;

  public void setImages(List<FacebookImage> images) {
    this.images = images;
  }
}
