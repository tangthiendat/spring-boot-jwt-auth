package com.ttdat.springbootjwt.controller;

import com.ttdat.springbootjwt.dto.request.AuthenticationRequest;
import com.ttdat.springbootjwt.dto.request.RegisterRequest;
import com.ttdat.springbootjwt.dto.response.AuthenticationResponse;
import com.ttdat.springbootjwt.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest registerRequest
    ) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> athenticate(
            @RequestBody AuthenticationRequest authenticationRequest
            ) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
}
