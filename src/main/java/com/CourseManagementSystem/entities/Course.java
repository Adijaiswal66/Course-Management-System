package com.CourseManagementSystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Course")
public class Course{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_seq_gen")
    @SequenceGenerator(name = "course_seq_gen", sequenceName = "course_seq", allocationSize = 1)
    private Long courseId;

    @Column(name = "Course_Name")
    private String courseName;

    @Column(name = "Course_Description")
    private String courseDescription;

    @ManyToMany(mappedBy = "courseList",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("courseList") // Ignore the courseList when serializing users
    private List<User> user = new ArrayList<>();

    @OneToOne(mappedBy = "course")
    private Assignment assignment;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public Assignment getAssignments() {
        return assignment;
    }

    public void setAssignments(Assignment assignment) {
        this.assignment = assignment;
    }

    public Course() {
    }

    public Course(Long courseId, String courseName, String courseDescription, List<User> user, Assignment assignment) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.user = user;
        this.assignment = assignment;
    }
}
