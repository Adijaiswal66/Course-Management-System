package com.CourseManagementSystem.entities;


import lombok.*;

@Builder
public class JWTResponse {

    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "JWTResponse{" +
                "token='" + token + '\'' +
                '}';
    }

    public JWTResponse(String token) {
        this.token = token;
    }

}
