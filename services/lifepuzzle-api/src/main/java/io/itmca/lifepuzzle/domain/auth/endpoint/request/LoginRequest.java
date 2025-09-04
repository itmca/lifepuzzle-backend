package io.itmca.lifepuzzle.domain.auth.endpoint.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.itmca.lifepuzzle.global.util.MaskedSerializer;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Username is required")
    String username,
    
    @JsonSerialize(using = MaskedSerializer.class)
    @NotBlank(message = "Password is required")
    String password,
    
    String shareKey
) {}
