package io.itmca.lifepuzzle.domain.user.endpoint.request;

public record UserWithdrawRequest(
    String socialToken
) {
}