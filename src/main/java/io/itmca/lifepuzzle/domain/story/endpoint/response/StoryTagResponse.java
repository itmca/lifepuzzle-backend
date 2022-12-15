package io.itmca.lifepuzzle.domain.story.endpoint.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoryTagResponse {
    String key;
    String displayName;
    int priority;
}
