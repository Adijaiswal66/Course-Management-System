package com.CourseManagementSystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Entity
@Table(name = "Users")
//@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

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

    private Boolean isAccountLocked;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("user") // Ignore the user when serializing courses
    private List<Course> courseList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AssignmentSubmission> assignmentSubmissionList;

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public List<AssignmentSubmission> getAssignmentSubmissionList() {
        return assignmentSubmissionList;
    }

    public void setAssignmentSubmissionList(List<AssignmentSubmission> assignmentSubmissionList) {
        this.assignmentSubmissionList = assignmentSubmissionList;
    }

    public Boolean getAccountLocked() {
        return isAccountLocked;
    }

    public void setAccountLocked(Boolean accountLocked) {
        isAccountLocked = accountLocked;
    }

    public User() {
    }

    public User(Long userId, String name, String email, String password, Boolean isAccountLocked) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAccountLocked = isAccountLocked;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isAccountLocked=" + isAccountLocked +
                ", courseList=" + courseList +
                ", assignmentSubmissionList=" + assignmentSubmissionList +
                '}';
    }
}
