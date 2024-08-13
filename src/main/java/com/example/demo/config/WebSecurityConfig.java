package com.example.demo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import com.example.demo.authprovider.OauthTokenAuthenticationProvider;
import com.example.demo.authprovider.RememberMeAuthenticationProvider;
import com.example.demo.filter.RememberMeAuthenticationFilter;
import com.example.demo.filter.OauthTokenAuthenticationFilter;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;


@Profile("!local")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  private static final String PUBLIC = "/public/**";
  private static final String ACTUATOR = "/actuator/**";
  private static final String SAML = "/saml/**";
  private static final String CXML = "/cxml/**";
  private static final String ERROR = "/error";

  public static final String[] NONE_AUTHENTICATED_PATHS = {PUBLIC, ACTUATOR, SAML, CXML, ERROR};

  @Bean
  public AuthenticationManager authenticationManager(RememberMeAuthenticationProvider rememberMeAuthenticationProvider,
      OauthTokenAuthenticationProvider oauthTokenAuthenticationProvider) {
    return new ProviderManager(List.of(oauthTokenAuthenticationProvider,rememberMeAuthenticationProvider));
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

    final OauthTokenAuthenticationFilter oauthTokenAuthenticationFilter = new OauthTokenAuthenticationFilter();
    oauthTokenAuthenticationFilter.setAuthenticationManager(authenticationManager);
    final RememberMeAuthenticationFilter rememberMeAuthenticationFilter = new RememberMeAuthenticationFilter();
    rememberMeAuthenticationFilter.setAuthenticationManager(authenticationManager);

    http
        .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(httpSecurityHttpBasicConfigurer ->
            httpSecurityHttpBasicConfigurer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .addFilterBefore(oauthTokenAuthenticationFilter,
            AbstractPreAuthenticatedProcessingFilter.class)
        .addFilterBefore(rememberMeAuthenticationFilter,
            AbstractPreAuthenticatedProcessingFilter.class)
        .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
        authorizationManagerRequestMatcherRegistry
            .requestMatchers(NONE_AUTHENTICATED_PATHS)
            .permitAll()
            .requestMatchers(HttpMethod.GET, "/auth/oauth2/test/**")
            .hasAnyAuthority("SCOPE_write:invoice-notification")
            .anyRequest().authenticated())
        .authenticationManager(authenticationManager);

    return http.build();
  }

}
