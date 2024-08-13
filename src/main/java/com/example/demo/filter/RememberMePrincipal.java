package com.example.demo.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RememberMePrincipal {

  private String userId;
  private String rememberMeToken;


}
