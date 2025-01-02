package com.CourseManagementSystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Assignment")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long assignmentID;

    private String assignmentName;

    private String assignmentDescription;

    private Date assignmentDueDate;

    private Boolean isAssignmentSubmitted;

    @OneToOne
    @JsonBackReference
    private Course course;

    public Long getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(Long assignmentID) {
        this.assignmentID = assignmentID;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentDescription() {
        return assignmentDescription;
    }

    public void setAssignmentDescription(String assignmentDescription) {
        this.assignmentDescription = assignmentDescription;
    }

    public Date getAssignmentDueDate() {
        return assignmentDueDate;
    }

    public void setAssignmentDueDate(Date assignmentDueDate) {
        this.assignmentDueDate = assignmentDueDate;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Boolean getAssignmentSubmitted() {
        return isAssignmentSubmitted;
    }

    public void setAssignmentSubmitted(Boolean assignmentSubmitted) {
        isAssignmentSubmitted = assignmentSubmitted;
    }

    public Assignment() {
    }

    public Assignment(Long assignmentID, String assignmentName, String assignmentDescription, Date assignmentDueDate, Boolean isAssignmentSubmitted, Course course) {
        this.assignmentID = assignmentID;
        this.assignmentName = assignmentName;
        this.assignmentDescription = assignmentDescription;
        this.assignmentDueDate = assignmentDueDate;
        this.isAssignmentSubmitted = isAssignmentSubmitted;
        this.course = course;
    }
}
