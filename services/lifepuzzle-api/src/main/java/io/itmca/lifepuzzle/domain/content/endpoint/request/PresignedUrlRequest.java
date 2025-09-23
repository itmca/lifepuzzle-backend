package io.itmca.lifepuzzle.domain.content.endpoint.request;

import io.itmca.lifepuzzle.domain.content.type.AgeGroup;
import jakarta.validation.constraints.Size;
import java.util.List;

public record PresignedUrlRequest(Long heroId, AgeGroup ageGroup, @Size(max = 30) List<FileUploadDto> files) {
    
    public record FileUploadDto(String fileName, String contentType) {}
}
