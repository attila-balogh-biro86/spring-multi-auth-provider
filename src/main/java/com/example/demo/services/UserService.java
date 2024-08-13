package com.example.demo.services;

import java.util.Optional;

public interface UserService {

  Optional<User> buildUserByIdWithRememberMe(String userEncodedId, String token);
}
