package com.example.demo.filter;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import jakarta.servlet.http.HttpServletRequest;

public class OauthTokenAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

  @Override
  protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
    // Extract the token from the Authorization header
    String authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      return authorizationHeader.substring(7);
    }
    return null;
  }

  @Override
  protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
    return "N/A"; // No credentials are needed as token is pre-validated
  }
}
