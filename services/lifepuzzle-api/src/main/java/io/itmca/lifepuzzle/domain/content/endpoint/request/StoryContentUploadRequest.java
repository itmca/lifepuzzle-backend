package io.itmca.lifepuzzle.domain.content.endpoint.request;

/**
 * Request DTO for creating or updating story content by gallery.
 *
 * @param heroId hero ID
 * @param galleryId gallery ID
 * @param content story content
 */
public record StoryContentUploadRequest(Long heroId, Long galleryId, String content) {
}
