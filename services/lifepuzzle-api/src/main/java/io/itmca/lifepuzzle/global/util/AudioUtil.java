package io.itmca.lifepuzzle.global.util;

import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.XMPDM;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
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
        // Duration might be in milliseconds, convert to seconds
        double durationMs = Double.parseDouble(duration);
        return (int) Math.ceil(durationMs / 1000.0);
      }

      log.warn("Could not extract duration from audio file: {}", audioFile.getOriginalFilename());
      return null;

    } catch (IOException | SAXException | TikaException e) {
      log.error("Error extracting duration from audio file: {}", audioFile.getOriginalFilename(), e);
      return null;
    }
  }
}
