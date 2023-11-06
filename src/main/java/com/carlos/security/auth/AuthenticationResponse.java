package com.carlos.security.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
  @JsonProperty("acess_token")
  private String acessToken;

  @JsonProperty("refresh_token")
  private String refreshToken;

}
