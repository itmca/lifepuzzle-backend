package io.itmca.lifepuzzle.domain.ai.endpoint.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.itmca.lifepuzzle.global.aop.HeroNo;
import io.swagger.v3.oas.annotations.media.Schema;

public record AiPhotoGenerateRequest(
    @HeroNo
    @JsonAlias("heroNo")
    @Schema(description = "주인공 식별자")
    Long heroId,
    @Schema(description = "갤러리 식별자")
    Long galleryId,
    @Schema(description = "드라이빙 비디오 식별자")
    Long drivingVideoId) {
}