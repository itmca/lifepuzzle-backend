package io.itmca.lifepuzzle.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.tika.metadata.Metadata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class AudioUtilTest {

  @DisplayName("null or empty file returns null")
  @Test
  void returnsNullForMissingFile() {
    assertThat(AudioUtil.extractDuration(null)).isNull();

    var emptyFile = new MockMultipartFile("voice", "voice.wav", "audio/wav", new byte[0]);
    assertThat(AudioUtil.extractDuration(emptyFile)).isNull();
  }

  @DisplayName("audio duration is parsed as seconds for common audio types")
  @Test
  void parsesAudioDurationInSeconds() {
    Metadata videoMetadata = new Metadata();
    videoMetadata.set(Metadata.CONTENT_TYPE, "video/mp4");
    assertThat(AudioUtil.parseDurationSeconds("6.0", videoMetadata)).isEqualTo(6.0);

    Metadata audioMetadata = new Metadata();
    audioMetadata.set(Metadata.CONTENT_TYPE, "audio/mp4");
    assertThat(AudioUtil.parseDurationSeconds("6000", audioMetadata)).isEqualTo(6.0);

    Metadata mp3Metadata = new Metadata();
    mp3Metadata.set(Metadata.CONTENT_TYPE, "audio/mpeg");
    assertThat(AudioUtil.parseDurationSeconds("PT1S", mp3Metadata)).isEqualTo(1.0);
  }
}
