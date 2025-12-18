package io.itmca.lifepuzzle.domain.content.endpoint.request;

import io.itmca.lifepuzzle.domain.content.type.AgeGroup;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Request DTO for updating gallery metadata.
 *
 * @param date gallery date
 * @param ageGroup age group category
 */
public record GalleryUpdateRequest(
    LocalDate date,

    @NotNull(message = "Age group is required")
    AgeGroup ageGroup
) {
}
