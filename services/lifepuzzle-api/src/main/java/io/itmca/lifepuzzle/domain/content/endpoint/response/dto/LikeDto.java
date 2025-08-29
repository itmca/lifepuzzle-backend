package io.itmca.lifepuzzle.domain.content.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.content.entity.Like;
import io.itmca.lifepuzzle.domain.content.type.LikeType;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class LikeDto {
  private Long userNo;
  private LikeType type;
  private String targetId;

  public static LikeDto from(Like like) {
    return LikeDto.builder()
        .userNo(like.getUserId())
        .type(like.getType())
        .targetId(like.getTargetId())
        .build();
  }

  public static List<LikeDto> listFrom(List<Like> likes) {
    return likes.stream()
        .map(LikeDto::from)
        .toList();
  }
}
