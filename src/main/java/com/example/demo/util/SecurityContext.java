package com.example.demo.util;

import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.services.User;
import com.littlebig.toolkit.utils.Assertion;

public class SecurityContext {

  private SecurityContext() {
    //empty on purpose
  }

  public static AuthenticatedUser getAuthenticatedUser() {
    return Optional.of(SecurityContextHolder.getContext())
        .map(org.springframework.security.core.context.SecurityContext::getAuthentication)
        .filter(AuthenticatedUser.class::isInstance)
        .map(AuthenticatedUser.class::cast)
        .orElseThrow(() -> new IllegalArgumentException("No authenticated user"));
  }

  public static boolean isAuthenticated() {
    return Optional.of(SecurityContextHolder.getContext())
        .map(org.springframework.security.core.context.SecurityContext::getAuthentication)
        .filter(notAnonymousAuthenticationPredicate)
        .map(Authentication::isAuthenticated)
        .orElse(false);
  }

  public static AuthenticatedUser authenticateUser(User user) {
    Assertion.notNull(user, "user cannot be null");
    Assertion.notNull(user.getUserId(), "user id cannot be null");
    Assertion.isTrue(user.getUserId() > 0, "user id must be a valid one");
    var authenticatedUser = new AuthenticatedUser();
    authenticatedUser.setUserId(user.getUserId());
    authenticatedUser.setEncodedUserId(user.getUserEncodedId());
    Long companyId = user.getCompanyId();
    if (companyId != null) {
      authenticatedUser.setCompanyId(user.getCompanyId());
    }
    authenticatedUser.setAdminLoggedAsId(user.getAdminId());
    authenticatedUser.setEncodedAdminLoggedAsId(user.getAdminEncodedId());

    SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    return authenticatedUser;
  }

  private static final Predicate<Authentication> notAnonymousAuthenticationPredicate = authentication -> !(authentication instanceof AnonymousAuthenticationToken);

}
