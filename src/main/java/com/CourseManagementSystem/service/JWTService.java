package com.CourseManagementSystem.service;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    private static final long JWT_TOKEN_VALIDITY = 60 * 60 * 30;
    private final String secretKey;

    public JWTService() {
        Dotenv dotenv = Dotenv.load();
        this.secretKey = dotenv.get("SECRET_KEY");
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + +JWT_TOKEN_VALIDITY)).signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }
}
