package com.example.demo.util;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AuthenticatedUser implements Authentication {

  @Include
  private Long userId;
  private String encodedUserId;
  private String appellation;
  private Long companyId;
  private Long adminLoggedAsId;
  private String encodedAdminLoggedAsId;
  private Locale locale;

  @Override
  public String getName() {
    return String.valueOf(userId);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public Object getCredentials() {
    return encodedUserId.hashCode();
  }

  @Override
  public Object getDetails() {
    return toString();
  }

  @Override
  public Object getPrincipal() {
    return encodedUserId;
  }

  @Override
  public boolean isAuthenticated() {
    return userId != null && userId > 0;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    throw new IllegalArgumentException("cannot be set here, please set the userId");
  }
}
