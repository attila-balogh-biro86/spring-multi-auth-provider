package com.example.demo.services;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class User {

  private static final String ANONYME = "Anonyme";
  private Long adminId;
  private String adminEncodedId;
  private String userEncodedId;
  private Long userId;
  private Long companyId;
  private String firstName;
  private String lastName;

}
