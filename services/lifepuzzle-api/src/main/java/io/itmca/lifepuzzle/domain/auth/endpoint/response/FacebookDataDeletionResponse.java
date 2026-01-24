package io.itmca.lifepuzzle.domain.auth.endpoint.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FacebookDataDeletionResponse(
    String url,
    @JsonProperty("confirmation_code") String confirmationCode
) {}
