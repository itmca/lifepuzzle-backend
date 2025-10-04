package io.itmca.lifepuzzle.domain.ai.endpoint.request;

import io.itmca.lifepuzzle.global.aop.HeroNo;
import io.swagger.v3.oas.annotations.media.Schema;

public record AiPhotoGenerateRequest(
    @HeroNo
    @Schema(description = "주인공 식별자")
    Long heroNo,
    @Schema(description = "갤러리 식별자")
    Long galleryId,
    @Schema(description = "드라이빙 비디오 식별자")
    Long drivingVideoId) {
}