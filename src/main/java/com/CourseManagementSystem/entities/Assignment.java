package com.CourseManagementSystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Assignment")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentID;

    @NotBlank(message = "Assignment name cannot be empty")
    private String assignmentName;

    @NotBlank(message = "Please enter description")
    private String assignmentDescription;

    private Date assignmentDueDate;

    @OneToOne
    @JsonBackReference
    private Course course;

    @OneToMany(mappedBy = "assignment",cascade = CascadeType.ALL)
    private List<AssignmentSubmission> assignmentSubmissionList;

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

    public List<AssignmentSubmission> getAssignmentSubmissionList() {
        return assignmentSubmissionList;
    }

    public void setAssignmentSubmissionList(List<AssignmentSubmission> assignmentSubmissionList) {
        this.assignmentSubmissionList = assignmentSubmissionList;
    }

    //    public Boolean getAssignmentSubmitted() {
//        return isAssignmentSubmitted;
//    }
//
//    public void setAssignmentSubmitted(Boolean assignmentSubmitted) {
//        isAssignmentSubmitted = assignmentSubmitted;
//    }

    public Assignment() {
    }

    public Assignment(Long assignmentID, String assignmentName, String assignmentDescription, Date assignmentDueDate, Boolean isAssignmentSubmitted, Course course) {
        this.assignmentID = assignmentID;
        this.assignmentName = assignmentName;
        this.assignmentDescription = assignmentDescription;
        this.assignmentDueDate = assignmentDueDate;
//        this.isAssignmentSubmitted = isAssignmentSubmitted;
        this.course = course;
    }
}
