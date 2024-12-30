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
    public ResponseEntity<String> addCourse(@RequestBody Course course) {
        return this.courseService.addCourse(course);
    }

    @GetMapping(value = "/view-courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        return this.courseService.getAllCourses();
    }

    @GetMapping(value = "/view-course/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable("courseId") Long courseId) {
        return this.courseService.getCourseById(courseId);
    }

    @PutMapping(value = "/edit-course/{courseId}")
    public ResponseEntity<String> editCourse(@PathVariable("courseId") Long courseId, @RequestBody Course course) {
        return this.courseService.editCourse(courseId, course);
    }

    @DeleteMapping(value = "/delete-course/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable("courseId") Long courseId, @RequestBody Course course) {
        return this.courseService.deleteCourse(courseId, course);
    }

    @PostMapping(value = "/assign-course/{userId}/{courseId}")
    public ResponseEntity<String> assignCourse(@PathVariable("userId") Long userId, @PathVariable("courseId") Long courseId) {
        return this.courseService.assignCourse(userId, courseId);
    }


}
