package com.visionflow.config.security;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtService(
            @Value("${application.security.jwt.secret-key}") String secretKey,
            @Value("${application.security.jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${application.security.jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateAccessToken(String subject, Map<String, Object> claims) {
        return buildToken(subject, claims, accessTokenExpiration);
    }

    public String generateRefreshToken(String subject) {
        return buildToken(subject, Map.of(), refreshTokenExpiration);
    }

    public String extractSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String expectedSubject) {
        try {
            Claims claims = parseClaims(token);
            return expectedSubject.equals(claims.getSubject()) && !isExpired(claims);
        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("Invalid JWT token: {}", ex.getMessage());
            return false;
        }
    }

    public long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    private String buildToken(String subject, Map<String, Object> extraClaims, long expiration) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expiration))
                .signWith(signingKey)
                .compact();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
