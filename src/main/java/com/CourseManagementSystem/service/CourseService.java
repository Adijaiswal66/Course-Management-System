package com.CourseManagementSystem.service;

import com.CourseManagementSystem.dao.CourseRepository;
import com.CourseManagementSystem.entities.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public ResponseEntity<String> addCourse(Course course) {
        Optional<Course> existingCourse = this.courseRepository.findByCourseName(course.getCourseName());
        if (existingCourse.isEmpty()) {
            try {
                this.courseRepository.save(course);
                return new ResponseEntity<>("Course is added successfully!!", HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return new ResponseEntity<>("Course already exist !!", HttpStatus.NOT_FOUND);

    }


    public ResponseEntity<List<Course>> getAllCourses() {
        if (this.courseRepository.count() >= 1) {
            try {
                List<Course> courseList = this.courseRepository.findAll();
                return new ResponseEntity<>(courseList, HttpStatus.OK);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
