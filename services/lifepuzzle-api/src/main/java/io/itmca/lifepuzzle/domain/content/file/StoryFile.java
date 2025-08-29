package io.itmca.lifepuzzle.domain.content.file;

import java.util.List;
import lombok.Builder;

@Builder
public record StoryFile(List<StoryImageFile> images,
                        List<StoryVoiceFile> voices,
                        List<StoryVideoFile> videos) {
}