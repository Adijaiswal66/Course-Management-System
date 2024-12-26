package com.CourseManagementSystem.service;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    private static final long JWT_TOKEN_VALIDITY = 60 * 60 * 30;

    private final String secretKey;
    private final String encodedKey;

    // Load .env file and initialize the secret key
    public JWTService() {
        Dotenv dotenv = Dotenv.configure().load(); // Load .env file
        this.secretKey = dotenv.get("SECRET_KEY");
        this.encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + +JWT_TOKEN_VALIDITY)).signWith(SignatureAlgorithm.HS512, encodedKey).compact();
    }
}
