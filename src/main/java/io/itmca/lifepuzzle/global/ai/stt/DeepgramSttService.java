package io.itmca.lifepuzzle.global.ai.stt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.itmca.lifepuzzle.global.exception.AiServiceUnavailableException;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeepgramSttService implements SpeechToTextService {
  @Value("${api.deepgram.key}")
  private String apiKey;
  @Value("${api.deepgram.uri}")
  private String uri;

  @Override
  public String transcribeAudio(CustomFile customFile) {
    try {
      HttpClient client = HttpClient.newHttpClient();

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(uri))
          .header("Authorization", "Token " + apiKey)
          .header("Content-Type", customFile.getContentType())
          .POST(HttpRequest.BodyPublishers.ofByteArray(customFile.getBytes()))
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(response.body());

      client.close();
      return rootNode.get("results").get("channels").get(0).get("alternatives").get(0)
          .get("paragraphs").get("transcript").asText();
    } catch (Exception e) {
      throw new AiServiceUnavailableException("Deepgram STT");
    }
  }
}
