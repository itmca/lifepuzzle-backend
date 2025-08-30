package io.itmca.lifepuzzle.domain.auth;

import java.util.List;

public record FacebookPhoto(
    List<FacebookImage> images,
    String id
) {}
