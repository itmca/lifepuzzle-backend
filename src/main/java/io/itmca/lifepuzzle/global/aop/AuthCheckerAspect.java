package io.itmca.lifepuzzle.global.aop;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.global.exception.HeroAccessDeniedException;
import io.itmca.lifepuzzle.global.exception.MissingHeroNoException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class AuthCheckerAspect {

  private final UserQueryService userQueryService;

  @Before("@annotation(io.itmca.lifepuzzle.global.aop.AuthCheck)")
  public void checkAuth(final JoinPoint joinPoint) throws IllegalAccessException {
    var method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    var authCheck = method.getAnnotation(AuthCheck.class);

    var heroNo = findHeroNoInArgs(joinPoint.getArgs(), method)
        .orElseThrow(MissingHeroNoException::new);

    validatePermission(heroNo, authCheck.auths());
  }

  private Optional<Long> findHeroNoInArgs(Object[] args, Method method) {
    var parameterAnnotations = method.getParameterAnnotations();

    for (int i = 0; i < args.length; i++) {
      if (args[i] == null) {
        continue;
      }

      for (var annotation : parameterAnnotations[i]) {
        if (annotation.annotationType() == HeroNo.class) {
          return Optional.ofNullable((Long) args[i]);
        }
        if (annotation.annotationType() == HeroNoContainer.class) {
          return getHeroNoFieldValue(args[i]);
        }
      }
    }
    return Optional.empty();
  }

  private Optional<Long> getHeroNoFieldValue(Object obj) {
    var fields = obj.getClass().getDeclaredFields();
    // heroNo 필드는 1depth만 고려
    for (var field : fields) {
      if (field.isAnnotationPresent(HeroNo.class)) {
        field.setAccessible(true);
        try {
          return Optional.ofNullable((Long) field.get(obj));
        } catch (IllegalAccessException e) {
          return Optional.empty();
        }
      }
    }
    return Optional.empty();
  }

  private void validatePermission(Long heroNo, HeroAuthStatus[] auths) {
    var principal = (AuthPayload) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
    var userNo = principal.getUserNo();

    var heroUserAuths = userQueryService.findByUserNo(userNo).getHeroUserAuths();

    if (!hasAccessPermission(heroUserAuths, heroNo, auths)) {
      throw new HeroAccessDeniedException();
    }
  }

  private boolean hasAccessPermission(List<HeroUserAuth> heroUserAuths, Long heroNo,
                                             HeroAuthStatus... auths) {
    return heroUserAuths.stream()
        .anyMatch(heroUserAuth ->
            hasMatchingHeroNo(heroUserAuth, heroNo) && hasMatchingAuth(heroUserAuth, auths)
        );
  }

  private boolean hasMatchingHeroNo(HeroUserAuth heroUserAuth, Long heroNo) {
    return heroUserAuth.getHero().getHeroNo().equals(heroNo);
  }

  private boolean hasMatchingAuth(HeroUserAuth heroUserAuth, HeroAuthStatus... auths) {
    return Arrays.asList(auths).contains(heroUserAuth.getAuth());
  }
}
