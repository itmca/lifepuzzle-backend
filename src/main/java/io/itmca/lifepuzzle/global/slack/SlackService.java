package io.itmca.lifepuzzle.global.slack;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SlackService {
  private final SlackProperties slackProperties;

  public SlackService(SlackProperties slackProperties) {
    this.slackProperties = slackProperties;
  }

  public void sendNoti(String format, Object... args) {
    String message = String.format(format, args);

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    String payload = "{\"text\": \"" + message + "\"}";

    HttpEntity<String> entity = new HttpEntity<>(payload, headers);

    restTemplate.postForObject(slackProperties.getWebhookUrl(), entity, String.class);
  }
}
