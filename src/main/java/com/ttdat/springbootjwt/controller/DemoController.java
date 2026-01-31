package com.ttdat.springbootjwt.controller;

import com.ttdat.springbootjwt.dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
public class DemoController {

  @GetMapping
  public BaseResponse<String> testToken() {
    return BaseResponse.of("Token is valid");
  }
}
