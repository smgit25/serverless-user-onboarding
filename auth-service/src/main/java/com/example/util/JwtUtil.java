package com.example.authservice.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    // in prod keep key in SSM/Secrets Manager
    private final Key key = Keys.hmacShaKeyFor("change-this-to-a-very-long-secret-key-of-at-least-256-bits".getBytes());
    private final long EXP = 1000L * 60 * 60 * 8; // 8 hours

    public String generateToken(String subject, String role) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXP))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> validate(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
