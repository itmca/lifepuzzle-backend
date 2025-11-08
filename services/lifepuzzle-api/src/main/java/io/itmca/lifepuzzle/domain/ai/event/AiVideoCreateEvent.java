package io.itmca.lifepuzzle.domain.ai.event;

import java.time.LocalDateTime;
import java.util.UUID;

record AiVideoCreateEvent(
    UUID id,
    Long photoId,
    Long drivingVideoId,
    LocalDateTime requestAt
) {
}
