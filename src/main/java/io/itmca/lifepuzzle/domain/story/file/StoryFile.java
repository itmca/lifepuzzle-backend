package io.itmca.lifepuzzle.domain.story.file;

import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import java.util.List;
import lombok.Builder;

@Builder
public record StoryFile(List<StoryImageFile> images, List<StoryVoiceFile> voices,
                        List<? extends CustomFile> videos) {
}
