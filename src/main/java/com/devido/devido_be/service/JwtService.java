package com.devido.devido_be.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String base64Secret;

    @Value("${app.jwt.verification-token-ttl-minutes}")
    private long ttlMinutes;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] secretBytes = java.util.Base64.getDecoder().decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(secretBytes);
    }

    public String generateEmailVerificationToken(String email) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + ttlMinutes * 60_000L);

        return Jwts.builder()
            .setSubject(email)                    // lưu email ở subject
            .setIssuedAt(now)
            .setExpiration(exp)
            .claim("type", "email_verification")  // thêm claim để phân biệt token loại khác
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        // kiểm tra kiểu token
        String type = claims.get("type", String.class);
        if (!"email_verification".equals(type)) {
            throw new IllegalArgumentException("Invalid token type");
        }
        return claims.getSubject();
    }
}

