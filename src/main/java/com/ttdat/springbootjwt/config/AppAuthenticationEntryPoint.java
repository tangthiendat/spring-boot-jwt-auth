package com.ttdat.springbootjwt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttdat.springbootjwt.dto.response.BaseResponse;
import com.ttdat.springbootjwt.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private final ObjectMapper mapper;

  private static final String MISSING_CREDENTIAL_EXCEPTION_CODE =
      "APP.MISSING_CREDENTIAL_EXCEPTION";

  private static final String MISSING_CREDENTIAL_EXCEPTION_MESSAGE =
      "You're missing credential to access this resource because full authentication is required";

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    String message;
    String exceptionCode;

    exceptionCode = MISSING_CREDENTIAL_EXCEPTION_CODE;
    message = MISSING_CREDENTIAL_EXCEPTION_MESSAGE;

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());

    mapper.writeValue(response.getWriter(), BaseResponse.error(exceptionCode, message));
  }
}
