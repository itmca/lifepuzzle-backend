package io.itmca.lifepuzzle.domain.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FacebookSignedRequestPayload(
    String algorithm,
    @JsonProperty("issued_at") Long issuedAt,
    @JsonProperty("user_id") String userId
) {}
