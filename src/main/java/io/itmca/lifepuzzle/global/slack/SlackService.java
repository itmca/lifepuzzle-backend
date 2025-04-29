package io.itmca.lifepuzzle.global.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SlackService {
  private final SlackProperties slackProperties;

  public void sendNoti(String format, Object... args) {
    var message = String.format(format, args);
    var payload = Map.of(
        "text", message,
        "mrkdwn", true
    );

    String payloadJson;
    try {
      payloadJson = new ObjectMapper().writeValueAsString(payload);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    var entity = new HttpEntity<>(payloadJson, headers);

    new RestTemplate().postForObject(slackProperties.getWebhookUrl(), entity, String.class);
  }
}
