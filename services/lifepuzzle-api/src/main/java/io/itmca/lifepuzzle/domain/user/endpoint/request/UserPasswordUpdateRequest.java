package io.itmca.lifepuzzle.domain.user.endpoint.request;

public record UserPasswordUpdateRequest(
    String oldPassword,
    String newPassword
) {
}
