package io.itmca.lifepuzzle.domain.content.endpoint.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record GalleryUploadCompleteRequest(
    @NotEmpty
    @Size(max = 30, message = "파일 키는 최대 30개까지 가능합니다")
    List<String> fileKeys
) {
}