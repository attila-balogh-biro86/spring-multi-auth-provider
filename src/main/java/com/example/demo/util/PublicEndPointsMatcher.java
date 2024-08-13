package com.example.demo.util;

import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.HttpServletRequest;

public final class PublicEndPointsMatcher implements RequestMatcher {

  private PublicEndPointsMatcher() {
    //empty on purpose
  }

  private static PublicEndPointsMatcher instance;

  public static PublicEndPointsMatcher getInstance() {
    if (instance == null) {
      instance = new PublicEndPointsMatcher();
    }
    return instance;
  }

  @Override
  public boolean matches(HttpServletRequest request) {
    return MatcherHelper.matchesAPublicEndpoint(request);
  }

}
