package io.itmca.lifepuzzle.global.monitoring;

import io.itmca.lifepuzzle.global.exception.handler.AlreadyExistsException;
import io.itmca.lifepuzzle.global.exception.handler.AuthException;
import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;
import io.sentry.Hint;
import io.sentry.SentryEvent;
import io.sentry.SentryOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CustomBeforeSendCallback implements SentryOptions.BeforeSendCallback {
  @Override
  public SentryEvent execute(SentryEvent event, Hint hint) {
    if (isLocalEnvOrRunningOnMacWindow(event) || hasAlertDisabledException(event)) {
      return null;
    }

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
