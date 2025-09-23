package io.itmca.lifepuzzle.domain.content.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.content.type.GalleryStatus;

public record GalleryUploadResultDto(
    String fileKey,
    Long galleryId,
    GalleryStatus status,
    String message
) {
}