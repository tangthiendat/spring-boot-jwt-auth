package com.ttdat.springbootjwt.interceptor;

import com.ttdat.springbootjwt.helper.IpHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class RequestLoggerInterceptor implements HandlerInterceptor {
  private static final String START_TIME = "startTime";

  @Override
  public boolean preHandle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler) {
    request.setAttribute(START_TIME, System.currentTimeMillis());
    return true;
  }

  @Override
  public void afterCompletion(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler,
      Exception ex) {
    long startTime = (long) request.getAttribute(START_TIME);
    long duration = System.currentTimeMillis() - startTime;

    String queryString = request.getQueryString();
    String path = request.getRequestURI() + (queryString != null ? "?" + queryString : "");

    if (ex != null) {
      log.error(
          "Exception occurred during [{} {}] -> status={} | duration={}ms | clientIP={}",
          request.getMethod(),
          path,
          response.getStatus(),
          duration,
          IpHelper.extractClientIp(request));
    } else {
      log.info(
          "{} {} -> status={} | duration={}ms | clientIP={}",
          request.getMethod(),
          path,
          response.getStatus(),
          duration,
          IpHelper.extractClientIp(request));
    }
  }
}
