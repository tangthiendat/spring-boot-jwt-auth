package com.ttdat.springbootjwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class JwtUtils {

    static Environment env;

    public static String extractUsername(String jwt) {
        return extractClaims(jwt).getSubject();
    }

    public static Date extractExpiration(String jwt){
        return extractClaims(jwt).getExpiration();
    }

//    private <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver){
//        final Claims claims = extractClaims(jwt);
//        //This allows the caller to specify which claim to extract
//        //by providing an appropriate claimsResolver function.
//        return claimsResolver.apply(claims);
//    }

    public static String generateToken(UserDetails userDetails){
        return generateToken(Map.of(), userDetails);
    }

    public static String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(extraClaims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                .signWith(getSecretKey()).compact();
    }

    public static boolean isTokenValid(String jwt, UserDetails userDetails){
        final String username = extractUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }

    private static boolean isTokenExpired(String jwt){
        return extractExpiration(jwt).before(new Date());
    }

    private static Claims extractClaims(String jwt){
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(jwt).getPayload();
     }

     private static SecretKey getSecretKey(){
         String jwtSecret = env.getProperty("JWT_SECRET");
         assert jwtSecret != null;
         return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
     }
}
