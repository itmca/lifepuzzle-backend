package io.itmca.lifepuzzle.global.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackService {
  private final SlackProperties slackProperties;
  private final Environment environment;

  public void sendNoti(String format, Object... args) {
    String[] activeProfiles = environment.getActiveProfiles();
    boolean isLocalProfile = java.util.Arrays.asList(activeProfiles).contains("local");
    boolean isTestProfile = java.util.Arrays.asList(activeProfiles).contains("test");
    boolean isMacOS = System.getProperty("os.name").toLowerCase().contains("mac");
    
    if (isLocalProfile || isTestProfile || isMacOS) {
      log.debug("Slack notification skipped - running in local, test profile: {} or Mac OS: {}",
          isLocalProfile, isMacOS);
      log.debug("Message that would have been sent: {}", String.format(format, args));
      return;
    }

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
