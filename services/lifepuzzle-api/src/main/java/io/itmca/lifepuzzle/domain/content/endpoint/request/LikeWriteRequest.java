package io.itmca.lifepuzzle.domain.content.endpoint.request;

import io.itmca.lifepuzzle.domain.content.type.LikeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LikeWriteRequest(
    @NotBlank(message = "Target ID is required")
    String targetId,
    
    @NotNull(message = "Like type is required")
    LikeType type
) {}
