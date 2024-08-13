package com.example.demo.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements UserService{

  @Override
  public Optional<User> buildUserByIdWithRememberMe(String userEncodedId, String token) {
    return Optional.of(new User.UserBuilder()
        .userEncodedId("1L")
        .userId(1L)
        .companyId(1000L)
        .adminId(100L)
        .adminEncodedId("100L")
        .firstName("Attila")
        .lastName("Balogh-Biro").build());
  }
}
