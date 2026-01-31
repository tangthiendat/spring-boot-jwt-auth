package com.ttdat.springbootjwt.service;

import com.ttdat.springbootjwt.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  @Value("${application.security.jwt.secret}")
  private String jwtSecret;

  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;

  @Value("${application.security.jwt.refresh-token.expiration}")
  private long refreshExpiration;

  public String extractUsername(String jwt) {
    return extractClaims(jwt).getSubject();
  }

  public Date extractExpiration(String jwt) {
    return extractClaims(jwt).getExpiration();
  }

  //    private <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver){
  //        final Claims claims = extractClaims(jwt);
  //        //This allows the caller to specify which claim to extract
  //        //by providing an appropriate claimsResolver function.
  //        return claimsResolver.apply(claims);
  //    }

  public String generateToken(UserDetails userDetails) {
    return generateToken(Map.of(), userDetails);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  public String generateRefreshToken(UserDetails userDetails) {
    return buildToken(new HashMap<>(), userDetails, refreshExpiration);
  }

  private String buildToken(
      Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
    return Jwts.builder()
        .subject(userDetails.getUsername())
        .claims(extraClaims)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSecretKey())
        .compact();
  }

  public boolean isTokenValid(String token) throws InvalidTokenException {
    if (token.isEmpty()) return false;
    try {
      Jwts.parser().verifyWith(getSecretKey()).build().parse(token);
    } catch (MalformedJwtException
        | SignatureException
        | ExpiredJwtException
        | UnsupportedJwtException
        | IllegalArgumentException exception) {
      throw new InvalidTokenException(exception.getMessage());
    }
    return true;
  }

  private Claims extractClaims(String jwt) {
    return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(jwt).getPayload();
  }

  private SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
  }
}
