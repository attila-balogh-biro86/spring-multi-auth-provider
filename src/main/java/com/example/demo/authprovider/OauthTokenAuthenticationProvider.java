package com.example.demo.authprovider;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.example.demo.filter.RememberMePrincipal;
import com.example.demo.services.User;
import com.example.demo.services.UserService;
import com.example.demo.util.AuthenticatedUser;
import com.example.demo.util.SecurityContext;

@Component
public class OauthTokenAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // Load the user details from the UserDetailsService
    final String oauthToken = (String) authentication.getPrincipal();
    String clientId = "";
    OAuth2User user = new OAuth2User() {
      @Override
      public Map<String, Object> getAttributes() {
        return Map.of();
      }

      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
      }

      @Override
      public String getName() {
        return "";
      }
    };

    return  new OAuth2AuthenticationToken(user, user.getAuthorities(), clientId);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return OAuth2AuthenticationToken.class.isAssignableFrom(authentication);
  }
}
