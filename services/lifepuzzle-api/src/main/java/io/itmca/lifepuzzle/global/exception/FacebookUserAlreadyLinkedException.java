package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AlreadyExistsException;

public class FacebookUserAlreadyLinkedException extends AlreadyExistsException {
  public FacebookUserAlreadyLinkedException(String facebookUserId) {
    super(String.format("Facebook user already linked - facebookUserId: %s", facebookUserId));
  }
}
