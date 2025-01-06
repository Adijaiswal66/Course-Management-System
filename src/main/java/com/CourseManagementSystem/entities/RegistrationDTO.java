package com.CourseManagementSystem.entities;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationDTO {

    @NotBlank(message = "Name can not be blank")
    @Column(name = "Name")
    private String name;

    @NotBlank(message = "Email field is mandatory")
    @Email
    @Column(name = "Email")
    private String email;

    @NotBlank(message = "Password field is mandatory")
    @Size(min = 6, message = "Password must be of minimum 6 characters")
    @Column(name = "Password")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
