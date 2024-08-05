package com.ttdat.springbootjwt.service;

import com.ttdat.springbootjwt.dto.request.AuthenticationRequest;
import com.ttdat.springbootjwt.dto.request.RegisterRequest;
import com.ttdat.springbootjwt.dto.response.AuthenticationResponse;
import com.ttdat.springbootjwt.entity.Role;
import com.ttdat.springbootjwt.entity.User;
import com.ttdat.springbootjwt.repository.UserRepository;
import com.ttdat.springbootjwt.util.JwtUtils;
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
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(Role.builder().roleName("USER").build()))
                .build();
        userRepository.save(user);
        String jwtToken = JwtUtils.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String jwtToken = JwtUtils.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
