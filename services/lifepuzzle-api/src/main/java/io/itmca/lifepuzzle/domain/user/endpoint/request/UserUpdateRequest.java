package io.itmca.lifepuzzle.domain.user.endpoint.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
  private String userNickName;
  @JsonProperty("isProfileImageUpdate")
  private boolean profileImageUpdate;
}
