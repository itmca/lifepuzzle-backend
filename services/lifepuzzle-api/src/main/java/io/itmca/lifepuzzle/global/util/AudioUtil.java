package io.itmca.lifepuzzle.global.util;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.XMPDM;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

@Slf4j
public class AudioUtil {

  /**
   * Extract duration from audio file in seconds.
   *
   * @param audioFile the audio file to extract duration from
   * @return duration in seconds, or null if extraction fails
   */
  @Nullable
  public static Integer extractDuration(MultipartFile audioFile) {
    if (audioFile == null || audioFile.isEmpty()) {
      return null;
    }

    try (InputStream stream = audioFile.getInputStream()) {
      AutoDetectParser parser = new AutoDetectParser();
      BodyContentHandler handler = new BodyContentHandler();
      Metadata metadata = new Metadata();

      parser.parse(stream, handler, metadata);

      // Try to get duration from metadata
      String duration = metadata.get(XMPDM.DURATION);
      if (duration != null) {
        double durationSeconds = parseDurationSeconds(duration, metadata);
        return (int) Math.ceil(durationSeconds);
      }

      log.warn("Could not extract duration from audio file: {}", audioFile.getOriginalFilename());
      return null;

    } catch (IOException | SAXException | TikaException e) {
      log.error("Error extracting duration from audio file: {}", audioFile.getOriginalFilename(), e);
      return null;
    }
  }

  static double parseDurationSeconds(String duration, Metadata metadata) {
    String trimmed = duration.trim();
    if (trimmed.startsWith("PT")) {
      try {
        return Duration.parse(trimmed).toMillis() / 1000.0;
      } catch (Exception ignored) {
        // Fall back to numeric parsing below.
      }
    }

    try {
      String normalized = trimmed.toLowerCase();
      double numeric = parseNumericDuration(normalized);
      DurationUnit unit = resolveDurationUnit(normalized, numeric, metadata);
      return unit == DurationUnit.MILLISECONDS ? numeric / 1000.0 : numeric;
    } catch (NumberFormatException e) {
      log.warn("Failed to parse duration value: {}", duration);
      return 0.0;
    }
  }

  private static double parseNumericDuration(String normalizedDuration) {
    String value = normalizedDuration;
    if (value.endsWith("ms")) {
      value = value.substring(0, value.length() - 2);
    } else if (value.endsWith("s")) {
      value = value.substring(0, value.length() - 1);
    }
    return Double.parseDouble(value.trim());
  }

  private static DurationUnit resolveDurationUnit(
      String normalizedDuration, double numeric, Metadata metadata) {
    if (normalizedDuration.endsWith("ms")) {
      return DurationUnit.MILLISECONDS;
    }
    if (normalizedDuration.endsWith("s")) {
      return DurationUnit.SECONDS;
    }

    String contentType = metadata.get(Metadata.CONTENT_TYPE);
    if (contentType != null && contentType.startsWith("video/")) {
      return DurationUnit.SECONDS;
    }

    // Heuristic: small values are more likely to be seconds than milliseconds.
    return numeric >= 1000.0 ? DurationUnit.MILLISECONDS : DurationUnit.SECONDS;
  }

  private enum DurationUnit {
    MILLISECONDS,
    SECONDS
  }
}
