package com.CourseManagementSystem.services;

import com.CourseManagementSystem.dao.CourseRepository;
import com.CourseManagementSystem.entities.Course;
import com.CourseManagementSystem.service.CourseService;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        this.courseService = new CourseService(this.courseRepository);
    }

    @Test
    public void addCourseTest_CourseExists() {
        Course course = new Course();
        course.setCourseName("TestCourse");

        when(courseRepository.findByCourseName("TestCourse")).thenReturn(Optional.of(course));

        ResponseEntity<String> response = courseService.addCourse(course);

        assertNotNull(response);
        assertEquals("TestCourse", course.getCourseName());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Course already exist !!", response.getBody());
    }

    @Test
    public void addCourseTest_CourseDoesNotExist() {
        Course course = new Course();
        course.setCourseName("TestCourse");

        when(courseRepository.findByCourseName("TestCourse")).thenReturn(Optional.empty());
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        ResponseEntity<String> response = courseService.addCourse(course);

        assertNotNull(response);
        assertEquals("TestCourse", course.getCourseName());
        assertEquals("Course is added successfully!!", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

//    @Test
//    public void getAllCoursesTest_CourseExists() {
//        Course course = new Course();
//        course.setCourseName("TestCourse");
//        Course course1 = new Course();
//        course.setCourseName("TestCourse1");
//
//        when(courseRepository.findAll()).thenReturn(List.of(course, course1));
//
//        ResponseEntity<List<Course>> response = courseService.getAllCourses();
//
//        System.out.println(response);
//        assertNotNull(response);
//        assertEquals(2, response.getBody().size());
//    }


}
