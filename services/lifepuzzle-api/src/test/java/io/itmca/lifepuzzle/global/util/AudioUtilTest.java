package io.itmca.lifepuzzle.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    byte[] wavBytes = buildWavBytes(8000, 1);
    var wavFile = new MockMultipartFile("voice", "voice.wav", "audio/wav", wavBytes);
    var mp4AudioFile = new MockMultipartFile("voice", "voice.m4a", "audio/mp4", wavBytes);
    var mp3File = new MockMultipartFile("voice", "voice.mp3", "audio/mpeg", wavBytes);

    assertThat(AudioUtil.extractDuration(wavFile)).isEqualTo(1);
    assertThat(AudioUtil.extractDuration(mp4AudioFile)).isEqualTo(1);
    assertThat(AudioUtil.extractDuration(mp3File)).isEqualTo(1);
  }

  private static byte[] buildWavBytes(int sampleRate, int seconds) {
    int bitsPerSample = 16;
    int channels = 1;
    int bytesPerSample = bitsPerSample / 8;
    int numSamples = sampleRate * seconds;
    int dataSize = numSamples * channels * bytesPerSample;
    int byteRate = sampleRate * channels * bytesPerSample;
    int blockAlign = channels * bytesPerSample;
    int riffChunkSize = 36 + dataSize;

    try {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      writeAscii(output, "RIFF");
      writeIntLE(output, riffChunkSize);
      writeAscii(output, "WAVE");
      writeAscii(output, "fmt ");
      writeIntLE(output, 16);
      writeShortLE(output, (short) 1);
      writeShortLE(output, (short) channels);
      writeIntLE(output, sampleRate);
      writeIntLE(output, byteRate);
      writeShortLE(output, (short) blockAlign);
      writeShortLE(output, (short) bitsPerSample);
      writeAscii(output, "data");
      writeIntLE(output, dataSize);
      output.write(new byte[dataSize]);
      return output.toByteArray();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to build WAV bytes", e);
    }
  }

  private static void writeAscii(ByteArrayOutputStream output, String value) throws IOException {
    output.write(value.getBytes());
  }

  private static void writeIntLE(ByteArrayOutputStream output, int value) throws IOException {
    output.write(value & 0xFF);
    output.write((value >> 8) & 0xFF);
    output.write((value >> 16) & 0xFF);
    output.write((value >> 24) & 0xFF);
  }

  private static void writeShortLE(ByteArrayOutputStream output, short value) throws IOException {
    output.write(value & 0xFF);
    output.write((value >> 8) & 0xFF);
  }
}
