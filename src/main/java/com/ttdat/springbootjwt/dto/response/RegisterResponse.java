package com.ttdat.springbootjwt.dto.response;

import com.ttdat.springbootjwt.entity.Role;
import java.util.UUID;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
  private UUID userId;
  private String firstName;
  private String lastName;
  private String email;
  private Role role;
}
