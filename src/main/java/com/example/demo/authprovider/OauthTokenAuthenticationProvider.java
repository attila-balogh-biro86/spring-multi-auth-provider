package com.example.demo.authprovider;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class OauthTokenAuthenticationProvider implements AuthenticationProvider {

  @Value( "${finally.client.id}")
  private String clientId;

  @Value( "${auth0.jwks.uri}")
  private String jwksURI;

  @Value( "${auth0.jwks.key.id}")
  private String signInKeyId;

  private static final Logger LOG = LoggerFactory.getLogger(OauthTokenAuthenticationProvider.class);

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // Load the user details from the UserDetailsService
    if(authentication.getPrincipal() instanceof String oauthToken) {

      // Get the public key from Auth0
      try {
        JwkProvider provider  = new UrlJwkProvider(new URI(jwksURI).toURL());
        Jwk jwk = provider.get(signInKeyId);
        RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();
        // Parse and verify the JWT token
        Claims claims = Jwts.parser()
            .setSigningKey(publicKey)
            .parseClaimsJws(oauthToken)
            .getBody();

        // Access data in the token
        LOG.debug("Subject: {}",claims.getSubject());
        LOG.debug("Issuer: {}",claims.getIssuer());
        LOG.debug("Expiration: {}",claims.getExpiration());

        OAuth2User user = new OAuth2User() {
          @Override
          public Map<String, Object> getAttributes() {
            return Map.of();
          }

          @Override
          public Collection<? extends GrantedAuthority> getAuthorities() {
            return convert(claims);
          }

          @Override
          public String getName() {
            return claims.getAudience();
          }
        };
        return new OAuth2AuthenticationToken(user, user.getAuthorities(), clientId);
      }
      catch (Exception e) {
        LOG.error(e.getMessage());
        return null;
      }
    }
    return null;
  }

  public Collection<GrantedAuthority> convert(Claims jwt) {
    var scope = jwt.get("scope");
    return List.of(new SimpleGrantedAuthority("SCOPE_" + scope));
  }


  @Override
  public boolean supports(Class<?> authentication) {
    return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
