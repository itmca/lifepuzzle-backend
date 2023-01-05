package io.itmca.lifepuzzle.domain.story.endpoint.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoryTagResponse implements Comparable<StoryTagResponse> {
    String key;
    String displayName;
    Long priority;

    @Override
    public int compareTo(StoryTagResponse o) {
        return Long.compare(this.priority, o.priority);
    }
}
