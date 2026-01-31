package com.ttdat.springbootjwt.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ServletService {
  private static final String AUTH_HEADER = "Authorization";
  private static final String AUTH_BEARER = "Bearer ";
  private static final Integer JWT_POSITION = 7;

  public String parseTokenHeader(HttpServletRequest request) {
    String authHeader = request.getHeader(AUTH_HEADER);
    if (StringUtils.hasText(authHeader) && authHeader.startsWith(AUTH_BEARER))
      return authHeader.substring(JWT_POSITION);
    return "";
  }
}
