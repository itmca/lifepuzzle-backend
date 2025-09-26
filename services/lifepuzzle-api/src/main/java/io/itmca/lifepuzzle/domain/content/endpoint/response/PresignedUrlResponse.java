package io.itmca.lifepuzzle.domain.content.endpoint.response;

import java.util.List;

public record PresignedUrlResponse(List<PresignedUrlDto> presignedUrls) {

  public record PresignedUrlDto(String fileKey, String url, Headers headers) {

    public record Headers(String host, String contentType, String cacheControl) {}
  }
}