package io.itmca.lifepuzzle.global.ai.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.itmca.lifepuzzle.global.exception.AiServiceUnavailableException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAiChatService {
  @Value("${api.chat-gpt.key}")
  private String apiKey;
  @Value("${api.chat-gpt.uri}")
  private String uri;
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public String requestChat(String systemPrompt, String userPrompt) {
    try {
      Map<String, Object> requestBody = new HashMap<>();
      requestBody.put("model", "gpt-3.5-turbo");
      requestBody.put("messages", new Object[] {
          new HashMap<String, String>() {{
            put("role", "system");
            put("content", systemPrompt);
          }},
          new HashMap<String, String>() {{
            put("role", "user");
            put("content", userPrompt);
          }}
      });

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(uri))
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer " + apiKey)
          .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
          .build();

      HttpClient client = HttpClient.newHttpClient();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      JsonNode rootNode = objectMapper.readTree(response.body());

      client.close();
      return rootNode.path("choices").get(0)
          .path("message").path("content").asText();
    } catch (Exception e) {
      throw new AiServiceUnavailableException("Chatgpt");
    }
  }

}
