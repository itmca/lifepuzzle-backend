package io.itmca.lifepuzzle.domain.auth.endpoint.response;

import io.itmca.lifepuzzle.domain.auth.FacebookPhoto;
import java.util.List;
import lombok.Getter;

@Getter
public class FacebookPhotoResponse {
  private List<FacebookPhoto> data;
}
