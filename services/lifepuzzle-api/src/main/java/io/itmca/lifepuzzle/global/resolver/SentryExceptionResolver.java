package io.itmca.lifepuzzle.global.resolver;

import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class SentryExceptionResolver implements HandlerExceptionResolver, Ordered {
  
  @Override
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                       Object handler, Exception ex) {
    Sentry.captureException(ex);

    return null;
  }

  @Override
  public int getOrder() {
    return Integer.MIN_VALUE;
  }
}
