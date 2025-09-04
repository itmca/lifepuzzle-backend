package io.itmca.lifepuzzle.domain.content.endpoint.request;

import io.itmca.lifepuzzle.domain.content.type.AgeGroup;
import lombok.Getter;

public record GalleryWriteRequest(Long heroId, AgeGroup ageGroup) {
}
