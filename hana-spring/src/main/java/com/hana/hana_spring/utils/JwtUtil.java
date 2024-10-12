package com.hana.hana_spring.utils;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.exp}")
    private long expTime;

    @Value("${jwt.token-prefix}")
    private String tokenPrefix;

    public String generateToken(String id, Boolean admin) {
        return tokenPrefix + JWT.create()
                .withSubject(id)
                .withClaim("admin", admin)
                .withExpiresAt(new Date(System.currentTimeMillis() + expTime))
                .sign(Algorithm.HMAC512(secret));
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Integer getLoginUserId(String token) {
        try {
            String id = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getSubject();
            return Integer.valueOf(id);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean verify_admin(String token) {
        try {
            boolean admin = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getClaim("admin").asBoolean();
            if (admin) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
