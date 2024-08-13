package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import com.example.demo.authprovider.OauthTokenAuthenticationProvider;
import com.example.demo.authprovider.RememberMeAuthenticationProvider;
import com.example.demo.filter.RememberMeAuthenticationFilter;
import com.example.demo.filter.OauthTokenAuthenticationFilter;
import com.example.demo.services.UserService;


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
  private final RememberMeAuthenticationFilter authenticationFilter = new RememberMeAuthenticationFilter();

  @Autowired
  protected void configure(AuthenticationManagerBuilder auth,UserService userService) throws Exception {
    auth.authenticationProvider(new RememberMeAuthenticationProvider(userService))
        .authenticationProvider(new OauthTokenAuthenticationProvider());
  }
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(httpSecurityHttpBasicConfigurer ->
            httpSecurityHttpBasicConfigurer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .addFilterBefore(new OauthTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterAt(new RememberMeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
        authorizationManagerRequestMatcherRegistry
            .requestMatchers(NONE_AUTHENTICATED_PATHS)
            .permitAll()
            .requestMatchers(HttpMethod.GET, "/auth/oauth2/test/**")
            .hasAnyAuthority("SCOPE_somescope/test")
            .anyRequest().authenticated());

    return http.build();
  }
}
