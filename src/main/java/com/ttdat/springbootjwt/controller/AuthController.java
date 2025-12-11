package com.ttdat.springbootjwt.controller;

import com.ttdat.springbootjwt.dto.request.LoginRequest;
import com.ttdat.springbootjwt.dto.request.RegisterRequest;
import com.ttdat.springbootjwt.dto.response.BaseResponse;
import com.ttdat.springbootjwt.dto.response.AuthResponse;
import com.ttdat.springbootjwt.dto.response.RegisterResponse;
import com.ttdat.springbootjwt.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public BaseResponse<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
    return BaseResponse.of(authenticationService.register(registerRequest));
  }

  @PostMapping("/login")
  public BaseResponse<AuthResponse> login(
      @RequestBody LoginRequest authenticationRequest, HttpServletResponse servletResponse) {
    return BaseResponse.of(authenticationService.login(authenticationRequest, servletResponse));
  }
}
