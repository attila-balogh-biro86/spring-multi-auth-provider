package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/oauth2/test")
public class TokenTestController {

  private final Logger log = LoggerFactory.getLogger(TokenTestController.class);

  @GetMapping("/{id}")
  public String test(@PathVariable String id) {
    log.debug("Test auth controller called with the following ID: {}",id);
    return "The endpoint can be called with token, the ID is: "+id;
  }
}
