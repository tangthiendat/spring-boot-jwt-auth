package com.ttdat.springbootjwt.helper;

import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;

public class IpHelper {

  private IpHelper() {}

  /**
   * Extracts the client's IP address from the HttpServletRequest. It checks the "X-Forwarded-For"
   * header for public IPs first, then falls back to the remote address if no valid public IP is
   * found.
   *
   * @param request the HttpServletRequest object
   * @return the client's IP address as a String
   */
  public static String extractClientIp(HttpServletRequest request) {
    String header = request.getHeader("X-Forwarded-For");

    if (header != null && !header.isBlank()) {
      for (String ip : header.split(",")) {
        ip = ip.trim();
        if (!isPrivateIp(ip)) {
          return ip;
        }
      }
    }

    // Fallback to remote address
    return request.getRemoteAddr();
  }

  /**
   * Checks if the given IP address is a private IP.
   *
   * @param ip the IP address to check
   * @return true if the IP is private, false otherwise
   */
  private static boolean isPrivateIp(String ip) {
    try {
      InetAddress inet = InetAddress.getByName(ip);
      return inet.isAnyLocalAddress()
          || // 0.0.0.0
          inet.isLoopbackAddress()
          || // 127.x.x.x or ::1
          inet.isLinkLocalAddress()
          || // 169.254.x.x
          inet.isSiteLocalAddress(); // 10.x.x.x, 172.16.x.x - 172.31.x.x, 192.168.x.x
    } catch (Exception e) {
      return true;
    }
  }
}
