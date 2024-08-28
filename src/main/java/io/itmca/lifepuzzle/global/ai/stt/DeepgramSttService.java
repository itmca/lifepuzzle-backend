package io.itmca.lifepuzzle.global.ai.stt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeepgramSttService implements SpeechToTextService {

  @Override
  public String transcribeAudio(CustomFile customFile) {
    try {
      HttpClient client = HttpClient.newHttpClient();

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(
              "https://api.deepgram.com/v1/listen?model=base&smart_format=true&language=ko"))
          .header("Authorization", "Token ***REMOVED***")
          .header("Content-Type", "audio/m4a")
          .POST(HttpRequest.BodyPublishers.ofByteArray(customFile.getBytes()))
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      System.out.println("Response Code: " + response.statusCode());

      ObjectMapper objectMapper = new ObjectMapper();
      HashMap<String, Object> mapp = objectMapper.readValue(response.body(), HashMap.class);
      return (String) ((LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>)
          ((ArrayList) ((LinkedHashMap<String, Object>)
              ((ArrayList) ((LinkedHashMap<String, Object>) mapp.get("results")).get(
                  "channels")).get(0))
              .get("alternatives")).get(0)).get("paragraphs")).get("transcript");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return null;
  }
}
