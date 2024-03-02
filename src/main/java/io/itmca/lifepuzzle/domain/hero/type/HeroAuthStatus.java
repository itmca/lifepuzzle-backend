package io.itmca.lifepuzzle.domain.hero.type;

import io.swagger.v3.oas.annotations.media.Schema;

public enum HeroAuthStatus {
  @Schema(description = "이야기 뷰어 권한")
  VIEWER,
  @Schema(description = "댓글 작성 권한")
  COMMENTER,
  @Schema(description = "이야기 작성 권한")
  WRITER,
  @Schema(description = "관리자 권한")
  ADMIN,
  @Schema(description = "주인공 소유자")
  OWNER
}
