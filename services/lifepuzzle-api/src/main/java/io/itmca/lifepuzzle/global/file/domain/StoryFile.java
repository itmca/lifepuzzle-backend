package io.itmca.lifepuzzle.global.file.domain;

import java.util.List;
import lombok.Builder;

@Builder
public record StoryFile(List<StoryImageFile> images,
                        List<StoryVoiceFile> voices,
                        List<StoryVideoFile> videos) {
}