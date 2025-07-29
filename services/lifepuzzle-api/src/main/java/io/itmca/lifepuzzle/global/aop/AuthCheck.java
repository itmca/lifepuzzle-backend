package io.itmca.lifepuzzle.global.aop;

import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
  HeroAuthStatus[] auths();
}
