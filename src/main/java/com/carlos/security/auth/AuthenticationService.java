package com.carlos.security.auth;

import com.carlos.security.token.Token;
import com.carlos.security.token.TokenRepository;
import com.carlos.security.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import com.carlos.security.config.JwtService;
import com.carlos.security.user.Role;
import com.carlos.security.user.User;
import com.carlos.security.user.UserRepository;



import lombok.RequiredArgsConstructor;

import java.io.IOException;

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
    .mfaEnable(request.isMfaEnable())
    .build();
    // if MFA enable --> Generate Secrete
    if(request.isMfaEnable()){
      user.setSecret("");
    }
    repository.save(user);
    var jwtToken = jwtService.generateToken1(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .mfaEnable(user.isMfaEnable())
            .build();

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
    var jwtToken = jwtService.generateToken1(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
    .accessToken(jwtToken)
    .build();
  }

  private void revokeAllUserTokens(User user){
    var validUserTokens = tokenrepository.findAllValidTokenByUser(user.getId());
    if(validUserTokens .isEmpty())
      return;
    validUserTokens.forEach(t->{
      t.setExpired(true);
      t.setRevoked(true);
    });
    tokenrepository.saveAll(validUserTokens);

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


  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if(authHeader == null || !authHeader.startsWith("Bearer ")){
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);//todo extract the userEmail from JWT token
    if(userEmail != null ){
      var user = this.repository.findByEmail(userEmail).orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken1(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);


      }
    }
  }
}








