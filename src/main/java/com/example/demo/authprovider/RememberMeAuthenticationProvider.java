package com.example.demo.authprovider;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import com.example.demo.services.User;
import com.example.demo.filter.RememberMePrincipal;
import com.example.demo.services.UserService;
import com.example.demo.util.AuthenticatedUser;
import com.example.demo.util.SecurityContext;

@Component
public class RememberMeAuthenticationProvider implements AuthenticationProvider {

  private final UserService userService;

  @Autowired
  public RememberMeAuthenticationProvider(UserService service) {
    this.userService = service;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    if(authentication.getPrincipal() instanceof RememberMePrincipal principal){
      User user = userService.buildUserByIdWithRememberMe(principal.getUserId(),
          principal.getRememberMeToken()).get();
      return SecurityContext.authenticateUser(user);
    }
    return null;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
