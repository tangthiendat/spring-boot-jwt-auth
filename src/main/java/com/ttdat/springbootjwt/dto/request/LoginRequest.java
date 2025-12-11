package com.ttdat.springbootjwt.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
  private String email;
  private String password;
}
