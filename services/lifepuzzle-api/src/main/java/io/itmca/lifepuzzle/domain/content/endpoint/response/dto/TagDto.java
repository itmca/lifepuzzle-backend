package io.itmca.lifepuzzle.domain.content.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.content.type.AgeGroup;

public record TagDto(AgeGroup key, String label) {}
