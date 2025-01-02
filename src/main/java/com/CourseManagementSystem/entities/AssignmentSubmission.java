package com.CourseManagementSystem.entities;

import jakarta.persistence.*;
import lombok.Builder;

import java.util.Arrays;
import java.util.Date;

@Entity
@Builder
@Table(name = "AssignmentSubmission")
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentSubmissionId;

    private String assignmentSubmissionName;

    private Date assignmentSubmissionDate;

    private String type;

    @Lob
    @Column(name = "imagedata")
    private byte[] imageData;

    public Long getAssignmentSubmissionId() {
        return assignmentSubmissionId;
    }

    public void setAssignmentSubmissionId(Long assignmentSubmissionId) {
        this.assignmentSubmissionId = assignmentSubmissionId;
    }

    public String getAssignmentSubmissionName() {
        return assignmentSubmissionName;
    }

    public void setAssignmentSubmissionName(String assignmentSubmissionName) {
        this.assignmentSubmissionName = assignmentSubmissionName;
    }

    public Date getAssignmentSubmissionDate() {
        return assignmentSubmissionDate;
    }

    public void setAssignmentSubmissionDate(Date assignmentSubmissionDate) {
        this.assignmentSubmissionDate = assignmentSubmissionDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public AssignmentSubmission() {
    }

    @Override
    public String toString() {
        return "AssignmentSubmission{" +
                "assignmentSubmissionId=" + assignmentSubmissionId +
                ", assignmentSubmissionName='" + assignmentSubmissionName + '\'' +
                ", assignmentSubmissionDate=" + assignmentSubmissionDate +
                ", type='" + type + '\'' +
                ", imageData=" + Arrays.toString(imageData) +
                '}';
    }

    public AssignmentSubmission(Long assignmentSubmissionId, String assignmentSubmissionName, Date assignmentSubmissionDate, String type, byte[] imageData) {
        this.assignmentSubmissionId = assignmentSubmissionId;
        this.assignmentSubmissionName = assignmentSubmissionName;
        this.assignmentSubmissionDate = assignmentSubmissionDate;
        this.type = type;
        this.imageData = imageData;
    }
}
