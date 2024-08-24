package com.ttdat.springbootjwt.dto.response;

import com.ttdat.springbootjwt.dto.RoleDTO;
import com.ttdat.springbootjwt.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponse {
    UUID userId;
    String firstName;
    String lastName;
    String email;
    Set<RoleDTO> roles;
}
