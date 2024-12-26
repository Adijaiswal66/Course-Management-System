package com.CourseManagementSystem.controller;

import com.CourseManagementSystem.entities.Course;
import com.CourseManagementSystem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping(value = "/add-course")
    public ResponseEntity<String> addCourse(@RequestBody Course course){
        return this.courseService.addCourse(course);
    }

    @GetMapping(value = "/view-courses")
    public ResponseEntity<List<Course>> getAllCourses(){
        return this.courseService.getAllCourses();
    }
}
