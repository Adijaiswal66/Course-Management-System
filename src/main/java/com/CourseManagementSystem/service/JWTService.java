package com.CourseManagementSystem.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class JWTService {

    private static final long JWT_TOKEN_VALIDITY = 60 * 60 * 30;

    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();

        String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXC246824686868686";
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + +JWT_TOKEN_VALIDITY)).signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}
