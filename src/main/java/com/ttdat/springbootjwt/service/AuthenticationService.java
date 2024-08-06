package com.ttdat.springbootjwt.service;

import com.ttdat.springbootjwt.dto.request.AuthenticationRequest;
import com.ttdat.springbootjwt.dto.request.RegisterRequest;
import com.ttdat.springbootjwt.dto.response.AuthenticationResponse;
import com.ttdat.springbootjwt.dto.response.RegisterResponse;
import com.ttdat.springbootjwt.entity.Role;
import com.ttdat.springbootjwt.entity.User;
import com.ttdat.springbootjwt.mapper.UserMapper;
import com.ttdat.springbootjwt.repository.RoleRepository;
import com.ttdat.springbootjwt.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    UserMapper userMapper;

    public RegisterResponse register(RegisterRequest request) {
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = userMapper.toUser(request);
        user.setRoles(Set.of(userRole));
        return userMapper.toRegisterResponse(userRepository.save(user));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
