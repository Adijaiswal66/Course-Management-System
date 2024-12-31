package com.CourseManagementSystem.service;

import com.CourseManagementSystem.dao.CourseRepository;
import com.CourseManagementSystem.dao.UserRepository;
import com.CourseManagementSystem.entities.Course;
import com.CourseManagementSystem.entities.User;
import com.CourseManagementSystem.errors.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

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


    public ResponseEntity<Course> getCourseById(@PathVariable("courseId") Long courseId) {
        Optional<Course> existingCourse = this.courseRepository.findById(courseId);
        if (existingCourse.isPresent()) {
            Optional<Course> course = this.courseRepository.findById(courseId);
            return new ResponseEntity<>(course.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<String> editCourse(Long courseId, Course course) {
        Optional<Course> existingCourse = this.courseRepository.findById(courseId);
        if (existingCourse.isPresent()) {
            existingCourse.map(c -> {
                c.setCourseDescription(course.getCourseDescription());
                return this.courseRepository.save(c);
            });
            return new ResponseEntity<>("Course with id: " + courseId + " is updated successfully!!", HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteCourse(Long courseId, Course course) {
        Optional<Course> existingCourse = this.courseRepository.findById(courseId);
        if (existingCourse.isPresent()) {
            this.courseRepository.deleteById(courseId);
            return new ResponseEntity<>("Course with id: " + courseId + " is deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Course with id " + courseId + "does not exist!!", HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<String> assignCourse(Long userId, Long courseId) {
        User existingUser = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found !!"));
        Course existingCourse = this.courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found!!"));

        if (existingUser.getCourseList().contains(existingCourse)) {
            return new ResponseEntity<>("Course is already assigned to this user", HttpStatus.OK);
        }

        List<Course> courses = existingUser.getCourseList();
        courses.add(existingCourse);
        this.userRepository.save(existingUser);
        return new ResponseEntity<>("User with id: " + userId + " has been successfully assigned to course with id: " + courseId, HttpStatus.OK);
    }

    public ResponseEntity<String> removeCourse(Long userId, Long courseId) {
        User existingUser = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found !!"));
        Course existingCourse = this.courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found !!"));

        if (!existingUser.getCourseList().contains(existingCourse)) {
            return new ResponseEntity<>("User is not assigned with this course !!", HttpStatus.NOT_FOUND);
        }
        existingUser.getCourseList().remove(existingCourse);
        this.userRepository.save(existingUser);
        return new ResponseEntity<>("User is removed from this course !!", HttpStatus.OK);
    }
}
