package io.itmca.lifepuzzle.domain.user.endpoint.request;

import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @Size(min = 1, max = 50, message = "Nickname must be between 1 and 50 characters")
    String nickName,

    boolean profileImageUpdate
) {}
