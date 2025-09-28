package io.itmca.lifepuzzle.domain.auth.endpoint.response;

import java.util.List;

public record FacebookPhotosResponse(
    List<FacebookPhotoDto> photos
) {}