package io.itmca.lifepuzzle.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

// TODO: k8s 환경에서 요청 디버깅을 위한 것으로 디버깅 완료 후 삭제
@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    final String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
    final String method = request.getMethod();
    final String uri = request.getRequestURI();
    final String queryString = request.getQueryString();
    final String clientIp = getClientIp(request);
    final String userAgent = request.getHeader("User-Agent");

    StringBuilder logMessage = new StringBuilder();
    logMessage.append("HTTP Request - ");
    logMessage.append("Time: ").append(timestamp).append(", ");
    logMessage.append("Method: ").append(method).append(", ");
    logMessage.append("URI: ").append(uri);
    
    if (StringUtils.hasText(queryString)) {
      logMessage.append("?").append(queryString);
    }
    
    logMessage.append(", Client IP: ").append(clientIp);
    logMessage.append(", User-Agent: ").append(userAgent != null ? userAgent.substring(0, Math.min(100, userAgent.length())) : "N/A");

    // Add important headers
    final String contentType = request.getContentType();
    if (StringUtils.hasText(contentType)) {
      logMessage.append(", Content-Type: ").append(contentType);
    }

    final String authorization = request.getHeader("Authorization");
    if (StringUtils.hasText(authorization)) {
      logMessage.append(", Auth: ").append(authorization.startsWith("Bearer") ? "Bearer ***" : "***");
    }

    log.info(logMessage.toString());
    
    // Store start time for response logging
    request.setAttribute("startTime", System.currentTimeMillis());
    
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    final Long startTime = (Long) request.getAttribute("startTime");
    final long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;

    final String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
    final String method = request.getMethod();
    final String uri = request.getRequestURI();
    final int status = response.getStatus();

    StringBuilder logMessage = new StringBuilder();
    logMessage.append("HTTP Response - ");
    logMessage.append("Time: ").append(timestamp).append(", ");
    logMessage.append("Method: ").append(method).append(", ");
    logMessage.append("URI: ").append(uri).append(", ");
    logMessage.append("Status: ").append(status).append(", ");
    logMessage.append("Duration: ").append(duration).append("ms");

    if (ex != null) {
      logMessage.append(", Exception: ").append(ex.getClass().getSimpleName());
    }

    if (status >= 400) {
      log.warn(logMessage.toString());
    } else {
      log.info(logMessage.toString());
    }
  }

  private String getClientIp(HttpServletRequest request) {
    final String forwardedFor = request.getHeader("X-Forwarded-For");
    if (StringUtils.hasText(forwardedFor)) {
      return forwardedFor.split(",")[0].trim();
    }
    
    final String realIp = request.getHeader("X-Real-IP");
    if (StringUtils.hasText(realIp)) {
      return realIp;
    }
    
    return request.getRemoteAddr();
  }
}