package io.itmca.lifepuzzle.domain.auth.endpoint.request;

import io.itmca.lifepuzzle.domain.content.type.AgeGroup;

public record FacebookPhotoImportRequest(
    String code,
    Long heroNo,
    AgeGroup ageGroup
) {}
