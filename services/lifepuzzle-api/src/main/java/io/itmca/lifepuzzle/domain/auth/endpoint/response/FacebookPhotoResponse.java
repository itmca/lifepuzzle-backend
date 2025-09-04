package io.itmca.lifepuzzle.domain.auth.endpoint.response;

import io.itmca.lifepuzzle.domain.auth.FacebookPhoto;
import java.util.List;

public record FacebookPhotoResponse(
    List<FacebookPhoto> data
){}
