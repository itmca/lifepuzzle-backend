package io.itmca.lifepuzzle.domain.story.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.story.entity.Like;
import io.itmca.lifepuzzle.domain.story.type.LikeType;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class LikeDTO {
  private Long userNo;
  private LikeType type;
  private String targetId;

  public static LikeDTO from(Like like) {
    return LikeDTO.builder()
        .userNo(like.getUserId())
        .type(like.getType())
        .targetId(like.getTargetId())
        .build();
  }

  public static List<LikeDTO> listFrom(List<Like> likes) {
    return likes.stream()
        .map(LikeDTO::from)
        .toList();
  }
}
