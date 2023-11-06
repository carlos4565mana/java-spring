package com.carlos.security.auth;

import com.carlos.security.token.Token;
import com.carlos.security.token.TokenRepository;
import com.carlos.security.token.TokenType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.carlos.security.config.JwtService;
import com.carlos.security.user.Role;
import com.carlos.security.user.User;
import com.carlos.security.user.UserRepository;



import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final TokenRepository tokenrepository;
  
  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
    .firstname(request.getFirstname())
    .lastname(request.getLastname())
    .email(request.getEmail())
    .password(passwordEncoder.encode(request.getPassword()))
    .role(request.getRole())
    .build();
  var savedUser =  repository.save(user);
  var jwtToken = jwtService.generateToken(user);
  saveUserToken(savedUser, jwtToken);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
    .token(jwtToken)
    .build();

  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .revoked(false)
            .expired(false)
            .build();
    tokenrepository.save(token);
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
  authenticationManager.authenticate(
    new UsernamePasswordAuthenticationToken(
      request.getEmail(),
      request.getPassword()
    )
  );
  var user = repository.findByEmail(request.getEmail())
    .orElseThrow();
  var jwtToken = jwtService.generateToken(user);
  saveUserToken(user, jwtToken);
  return AuthenticationResponse.builder()
  .token(jwtToken)
  .build();
}

  
}
