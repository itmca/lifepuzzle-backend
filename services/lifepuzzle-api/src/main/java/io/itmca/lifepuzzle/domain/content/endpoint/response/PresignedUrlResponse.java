package io.itmca.lifepuzzle.domain.content.endpoint.response;

import io.itmca.lifepuzzle.domain.content.endpoint.response.dto.PresignedUrlDto;
import java.util.List;

public record PresignedUrlResponse(List<PresignedUrlDto> presignedUrls) {
}