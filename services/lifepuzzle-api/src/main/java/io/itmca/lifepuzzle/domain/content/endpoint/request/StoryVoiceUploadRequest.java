package io.itmca.lifepuzzle.domain.content.endpoint.request;

/**
 * Request DTO for creating or updating story voice by gallery.
 *
 * @param heroId hero ID
 * @param galleryId gallery ID
 */
public record StoryVoiceUploadRequest(Long heroId, Long galleryId) {
}
