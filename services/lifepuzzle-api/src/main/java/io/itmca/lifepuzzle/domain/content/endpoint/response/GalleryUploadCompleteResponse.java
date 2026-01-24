package io.itmca.lifepuzzle.domain.content.endpoint.response;

import io.itmca.lifepuzzle.domain.content.endpoint.response.dto.GalleryUploadResultDto;
import java.util.List;

public record GalleryUploadCompleteResponse(
    List<GalleryUploadResultDto> results,
    int successCount,
    int failureCount
) {
}