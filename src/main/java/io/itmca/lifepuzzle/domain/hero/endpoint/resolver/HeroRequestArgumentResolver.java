package io.itmca.lifepuzzle.domain.hero.endpoint.resolver;

import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroWriteRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class HeroRequestArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return HeroWriteRequest.class.isInstance(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
      throws Exception {
    System.out.println("webRequest getparameterMap");
    System.out.println(webRequest.getParameterMap());
    return null;
  }
}
