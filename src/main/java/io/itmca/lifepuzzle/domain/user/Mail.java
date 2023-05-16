package io.itmca.lifepuzzle.domain.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Mail {
  private String to;
  private String from;
  private String subject;
  private String html;
}
