package io.itmca.lifepuzzle.global.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class RequestTimingInterceptor implements HandlerInterceptor {

  private static final String START_TIME_ATTRIBUTE = "requestStartTime";

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());
    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      Exception ex) {
    var startTimeAttribute = request.getAttribute(START_TIME_ATTRIBUTE);
    if (!(startTimeAttribute instanceof Long)) {
      return;
    }

    long durationMillis = System.currentTimeMillis() - (Long) startTimeAttribute;
    log.info(
        "API {} {} completed with status {} in {}ms",
        request.getMethod(),
        getRequestPath(request),
        response.getStatus(),
        durationMillis);
  }

  private String getRequestPath(HttpServletRequest request) {
    String uri = request.getRequestURI();
    String query = request.getQueryString();
    return query == null || query.isEmpty() ? uri : uri + "?" + query;
  }
}
