package io.itmca.lifepuzzle.domain.hero.type;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public enum HeroAuthStatus {
  @Schema(description = "이야기 뷰어 권한")
  VIEWER(0),
  @Schema(description = "댓글 작성 권한")
  COMMENTER(0),
  @Schema(description = "이야기 작성 권한")
  WRITER(0),
  @Schema(description = "관리자 권한")
  ADMIN(4),
  @Schema(description = "주인공 소유자")
  OWNER(5);

  public final int priority;

  HeroAuthStatus(int priority) {
    this.priority = priority;
  }
}
