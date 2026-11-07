package com.MoneyBoard2.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private JwtService jwtService;
    private javax.crypto.SecretKey signingKey;

    @BeforeEach
    void setUp() {
        signingKey = Keys.hmacShaKeyFor(new byte[32]);
        jwtService = new JwtService(signingKey, 60_000);
    }

    @Test
    void generatedTokenCanBeValidated() {
        Long userId = 42L;
        String token = jwtService.generateToken(userId);

        Claims claims = jwtService.validateToken(token);

        assertEquals(userId.toString(), claims.getSubject());
    }

    @Test
    void retrievesUserIdFromToken() {
        String token = jwtService.generateToken(42L);

        assertEquals(42L, jwtService.getUserIdFromToken(token));
    }

    @Test
    void tokenValidityChecksSignatureAndExpiration() {
        String validToken = jwtService.generateToken(42L);
        String[] parts = validToken.split("\\.");
        parts[2] = (parts[2].startsWith("A") ? "B" : "A") + parts[2].substring(1);
        String alteredSignatureToken = String.join(".", parts);
        Instant now = Instant.now();
        String expiredToken = Jwts.builder()
                .subject("42")
                .issuedAt(Date.from(now.minusSeconds(120)))
                .expiration(Date.from(now.minusSeconds(60)))
                .signWith(signingKey)
                .compact();

        assertTrue(jwtService.isTokenValid(validToken));
        assertFalse(jwtService.isTokenValid(alteredSignatureToken));
        assertFalse(jwtService.isTokenValid(expiredToken));
        assertFalse(jwtService.isTokenValid("not-a-jwt"));
        assertFalse(jwtService.isTokenValid(null));
    }

    @Test
    void validationRejectsTokenWithAlteredSignature() {
        String[] parts = jwtService.generateToken(42L).split("\\.");
        parts[2] = (parts[2].startsWith("A") ? "B" : "A") + parts[2].substring(1);

        assertThrows(SignatureException.class,
                () -> jwtService.validateToken(String.join(".", parts)));
    }

    @Test
    void validationRejectsExpiredToken() {
        Instant now = Instant.now();
        String expiredToken = Jwts.builder()
                .subject("42")
                .issuedAt(Date.from(now.minusSeconds(120)))
                .expiration(Date.from(now.minusSeconds(60)))
                .signWith(signingKey)
                .compact();

        assertThrows(ExpiredJwtException.class, () -> jwtService.validateToken(expiredToken));
    }
}
