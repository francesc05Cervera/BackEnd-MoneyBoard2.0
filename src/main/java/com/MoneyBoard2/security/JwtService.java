package com.MoneyBoard2.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMillis;

    public JwtService(SecretKey signingKey, long expirationMillis) {
        if (signingKey == null) {
            throw new IllegalArgumentException("La chiave JWT è obbligatoria");
        }
        if (expirationMillis <= 0) {
            throw new IllegalArgumentException("La durata del token deve essere positiva");
        }
        this.signingKey = signingKey;
        this.expirationMillis = expirationMillis;
    }

    public String generateToken(Long subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Il subject del token è obbligatorio");
        }

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusMillis(expirationMillis);

        return Jwts.builder()
                .subject(subject.toString())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    public Claims validateToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Il token JWT è obbligatorio");
        }

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        String subject = validateToken(token).getSubject();
        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Il subject del token non contiene un ID utente valido", exception);
        }
    }
}
