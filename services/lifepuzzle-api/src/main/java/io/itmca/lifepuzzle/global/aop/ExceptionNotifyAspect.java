package io.itmca.lifepuzzle.global.aop;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.user.repository.UserRepository;
import io.itmca.lifepuzzle.global.slack.SlackService;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
@RequiredArgsConstructor
public class ExceptionNotifyAspect {

  private final SlackService slackService;
  private final UserRepository userRepository;
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Around("within(@org.springframework.web.bind.annotation.RestController *)")
  public Object notifyException(final ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      return joinPoint.proceed();
    } catch (Exception ex) {
      var request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
      var userNo = getUserInfo().orElse(-1L);
      var httpStatus = resolveHttpStatus(ex);

      var message = new StringBuilder();
      message.append("*예외 발생 알림*\n")
          .append("- *예외 타입*: ").append(ex.getClass().getSimpleName()).append("\n")
          .append("- *예외 메시지*: ").append(ex.getMessage()).append("\n")
          .append("- *발생 시간*: ").append(LocalDateTime.now().format(DATE_TIME_FORMATTER)).append("\n")
          .append("- *사용자 정보*: userId: ").append(userNo).append("\n")
          .append("- *요청 정보*: Method: ").append(request.getMethod())
          .append(", URI: ").append(request.getRequestURI())
          .append(", requestBody: ").append(getMaskedRequestBody(joinPoint).orElse(null)).append("\n")
          .append("- *예외 처리 결과*: Response sent: ")
          .append(httpStatus.value()).append(" ").append(httpStatus.getReasonPhrase()).append("\n")
          .append("- *스택 트레이스*: \n").append(getShortStackTrace(ex));

      slackService.sendNoti(message.toString());
      throw ex;
    }
  }

  private HttpStatus resolveHttpStatus(Exception ex) {
    var responseStatus = findAnnotation(ex.getClass(), ResponseStatus.class);
    if (responseStatus != null) {
      return responseStatus.value();
    }
    return HttpStatus.NOT_FOUND;
  }

  private Optional<Object> getRequestBody(ProceedingJoinPoint joinPoint) {
    var signature = (MethodSignature) joinPoint.getSignature();
    var method = signature.getMethod();
    var args = joinPoint.getArgs();
    var parameters = method.getParameters();

    var parts = new ArrayList<>();
    for (int i = 0; i < args.length; i++) {
      if (parameters[i].isAnnotationPresent(RequestBody.class)) {
        parts.add(args[i]);
      } else if (parameters[i].isAnnotationPresent(RequestPart.class)) {
        if (args[i] instanceof List) {
          var list = (List<?>) args[i];
          for (Object item : list) {
            if (!(item instanceof MultipartFile)) {
              parts.add(item);
            }
          }
        } else if (!(args[i] instanceof MultipartFile)) {
          parts.add(args[i]);
        }
      }
    }
    return parts.isEmpty() ? Optional.empty() : Optional.of(parts);
  }

  private Optional<Object> getMaskedRequestBody(ProceedingJoinPoint joinPoint) {
    return getRequestBody(joinPoint).map(this::convertToJson);
  }

  private String convertToJson(Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return "Failed to serialize requestBody";
    }
  }

  private String getShortStackTrace(Exception ex) {
    var sw = new StringWriter();
    var pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    var traceLines = sw.toString().lines().limit(5).toList();
    return String.join("\n", traceLines);
  }

  private Optional<Long> getUserInfo() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var principal = authentication.getPrincipal();
    if (principal == null || !(principal instanceof AuthPayload)) {
      return Optional.empty();
    }

    return Optional.of(((AuthPayload) principal).getUserId());
  }
}