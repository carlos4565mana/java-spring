package com.carlos.security.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.carlos.security.config.JwtService;
import com.carlos.security.user.Role;
import com.carlos.security.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  
  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
    .firstname(request.getFirstname())
    .lastname(request.getLastname())
    .email(request.getEmail())
    .password(passwordEncoder.encode(request.getPassword()))
    .role(Role.USER)
    .build();
  repository.save(user);
  var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
    .token(jwtToken)
    .build();
  }

public AuthenticationResponse authenticate(AuthenticationRequest request) {
  return null;
}

  
}
