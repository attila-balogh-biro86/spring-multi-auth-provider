package com.example.demo.filter;

import java.util.Arrays;
import java.util.Optional;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;


public class RememberMeAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

  private static final String USER_ID_HTTP_HEADER_NAME = "User-Id";
  private static final String COOKIE_REMEMBER_ME = "REMEMBER_ME";

  @Override
  protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {

    if(request.getHeader(USER_ID_HTTP_HEADER_NAME) != null) {
      return new RememberMePrincipal(request.getHeader(USER_ID_HTTP_HEADER_NAME),
          getToken(request.getCookies()));
    }
    return null;
  }

  @Override
  protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
    return "N/A";
  }

  private String getToken(Cookie[] cookies) {
    return Optional.ofNullable(cookies)
        .stream()
        .flatMap(Arrays::stream)
        .filter(cookie -> COOKIE_REMEMBER_ME.equals(cookie.getName()))
        .map(Cookie::getValue)
        .findFirst()
        .orElse(null);
  }
}


