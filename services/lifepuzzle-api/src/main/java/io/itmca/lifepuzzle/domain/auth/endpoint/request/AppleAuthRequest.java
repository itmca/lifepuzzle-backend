package io.itmca.lifepuzzle.domain.auth.endpoint.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AppleAuthRequest(
    @NotBlank(message = "Apple user ID is required")
    String appleUserId,
    
    @Email(message = "Invalid email format")
    String email,
    
    @NotBlank(message = "Identity token is required")
    String identityToken,
    
    String nonce,
    String shareKey
) {}