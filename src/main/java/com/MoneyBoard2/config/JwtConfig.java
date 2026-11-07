package com.MoneyBoard2.config;

import com.MoneyBoard2.security.JwtService;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtService jwtService(
            @Value("${jwt.secret}") String base64Secret,
            @Value("${jwt.expiration-ms:900000}") long expirationMillis) {
        return new JwtService(
                Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret)),
                expirationMillis);
    }
}
