package com.ttdat.springbootjwt.service;

import com.ttdat.springbootjwt.dto.request.LoginRequest;
import com.ttdat.springbootjwt.dto.request.RegisterRequest;
import com.ttdat.springbootjwt.dto.response.AuthResponse;
import com.ttdat.springbootjwt.dto.response.RegisterResponse;
import com.ttdat.springbootjwt.entity.Role;
import com.ttdat.springbootjwt.entity.User;
import com.ttdat.springbootjwt.mapper.UserMapper;
import com.ttdat.springbootjwt.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserMapper userMapper;

  public RegisterResponse register(RegisterRequest request) {
    request.setPassword(passwordEncoder.encode(request.getPassword()));
    User user = userMapper.toUser(request);
    user.setRole(Role.USER);
    return userMapper.toRegisterResponse(userRepository.save(user));
  }

  public AuthResponse login(LoginRequest request, HttpServletResponse servletResponse) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    User user =
        userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
    String accessToken = jwtService.generateToken(user);

    String refreshToken = jwtService.generateRefreshToken(user);

    // Store refresh token in http only cookie
    Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setAttribute("SameSite", "Strict");
    refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

    servletResponse.addCookie(refreshTokenCookie);

    return AuthResponse.builder().accessToken(accessToken).build();
  }
}
