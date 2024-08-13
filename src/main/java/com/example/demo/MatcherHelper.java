package com.example.demo;

import java.util.Arrays;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.http.HttpServletRequest;


public class MatcherHelper {

  private MatcherHelper() {
    //no public init
  }

  public static boolean matchesAPrivateEndpoint(HttpServletRequest request) {
    return Arrays.stream(WebSecurityConfig.NONE_AUTHENTICATED_PATHS)
        .map(AntPathRequestMatcher::new)
        .noneMatch(matcher -> matcher.matches(request));
  }

  public static boolean matchesAPublicEndpoint(HttpServletRequest request) {
    return !matchesAPrivateEndpoint(request);
  }

}
