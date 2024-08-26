package io.itmca.lifepuzzle.global.monitoring;

import io.itmca.lifepuzzle.global.exception.handler.AlreadyExistsException;
import io.itmca.lifepuzzle.global.exception.handler.AuthException;
import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;
import io.itmca.lifepuzzle.global.slack.SlackService;
import io.sentry.Hint;
import io.sentry.SentryEvent;
import io.sentry.SentryOptions;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CustomBeforeSendCallback implements SentryOptions.BeforeSendCallback {
  private final SlackService slackService;

  public CustomBeforeSendCallback(SlackService slackService) {
    this.slackService = slackService;
  }

  @Override
  public SentryEvent execute(SentryEvent event, Hint hint) {
    StringBuilder sb = new StringBuilder();

    if (isLocalEnvOrRunningOnMacWindow(event) || hasAlertDisabledException(event)) {
      return null;
    }

    Instant now = Instant.now();
    ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    String formattedTimestamp = zonedDateTime.format(formatter);

    sb.append(formattedTimestamp + "\n");
    sb.append("Message >>>>>" + event.getThrowable().getMessage() + "\n");

    StackTraceElement[] stackTrace = event.getThrowable().getStackTrace();

    for (int i = 0; i < stackTrace.length && i < 5; i++) {
      sb.append(stackTrace[i].toString()).append("\n");
    }
    sb.append("...\n");

    //슬랙에 메시시 전송
    slackService.sendNoti(sb.toString());

    return event;
  }

  private boolean isLocalEnvOrRunningOnMacWindow(SentryEvent event) {
    return "local".equals(event.getEnvironment())
        || StringUtils.startsWithIgnoreCase(System.getProperty("os.name"), "mac")
        || StringUtils.startsWithIgnoreCase(System.getProperty("os.name"), "windows");
  }

  private static boolean hasAlertDisabledException(SentryEvent event) {
    var throwable = event.getThrowable();

    return throwable instanceof NotFoundException
        || throwable instanceof AlreadyExistsException
        || throwable instanceof AuthException;
  }
}
