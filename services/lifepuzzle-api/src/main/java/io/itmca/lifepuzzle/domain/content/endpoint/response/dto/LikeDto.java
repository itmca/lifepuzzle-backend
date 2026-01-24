package io.itmca.lifepuzzle.domain.content.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.content.entity.Like;
import io.itmca.lifepuzzle.domain.content.type.LikeType;
import java.util.List;

public record LikeDto(
    Long userNo,
    LikeType type,
    String targetId
) {
  public static LikeDto from(Like like) {
    return new LikeDto(
        like.getUserId(),
        like.getType(),
        like.getContentId()
    );
  }

  public static List<LikeDto> listFrom(List<Like> likes) {
    return likes.stream()
        .map(LikeDto::from)
        .toList();
  }
}
