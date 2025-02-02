package com.CourseManagementSystem.services;

import com.CourseManagementSystem.dao.CourseRepository;
import com.CourseManagementSystem.dao.UserRepository;
import com.CourseManagementSystem.entities.Course;
import com.CourseManagementSystem.entities.User;
import com.CourseManagementSystem.errors.ResourceNotFoundException;
import com.CourseManagementSystem.service.CourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseService courseService;


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

    @Test
    public void getAllCoursesTest_CourseExists() {
        Course course = new Course();
        course.setCourseName("TestCourse");
        Course course1 = new Course();
        course1.setCourseName("TestCourse1");

        when(courseRepository.count()).thenReturn(2L);
        when(courseRepository.findAll()).thenReturn(new ArrayList<>(List.of(course, course1)));

        ResponseEntity<List<Course>> response = courseService.getAllCourses();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void getAllCourseTest_CourseDoesNotExists() {
        when(courseRepository.count()).thenReturn(0L);

        ResponseEntity<List<Course>> response = courseService.getAllCourses();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void getCourseByIdTest_CourseExists() {
        Course course = new Course();
        course.setCourseId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        ResponseEntity<Course> response = courseService.getCourseById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getCourseByIdTest_CourseDoesNotExists() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Course> response = courseService.getCourseById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void editCourseTest_CourseExists() {
        Course course = new Course();
        course.setCourseId(1L);
        course.setCourseName("TestCourse");
        course.setCourseDescription("TestDesc");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        course.setCourseDescription("TestDescription");
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        ResponseEntity<String> response = courseService.editCourse(1L, course);

        assertNotNull(response);
        assertEquals("TestDescription", course.getCourseDescription());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Course with id: 1 is updated successfully!!", response.getBody());
    }

    @Test
    public void editCourseTest_CourseDoesNotExists() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        Course course = new Course();

        ResponseEntity<String> response = courseService.editCourse(1L, course);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteCourseTest_CourseExists() {
        Course course = new Course();
        course.setCourseId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        ResponseEntity<String> response = courseService.deleteCourse(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteCourseTest_CourseDoesNotExists() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = courseService.deleteCourse(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void assignCourseTest_CourseAlreadyAssigned() {
        Course course = new Course();
        course.setCourseId(1L);

        User user = new User();
        user.setUserId(1L);
        user.setCourseList(new ArrayList<>(List.of(course)));  // Initialize the course list properly

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        ResponseEntity<String> response = courseService.assignCourse(1L, 1L);

        assertTrue(user.getCourseList().contains(course));  // Verify course is in the user's list
        assertEquals("Course is already assigned to this user", response.getBody());  // Expected message
        assertEquals(HttpStatus.OK, response.getStatusCode());  // Expected status code
    }

    @Test
    public void assignCourseTest_CourseNotAssigned() {
        Course course = new Course();
        course.setCourseId(1L);
        User user = new User();
        user.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        ResponseEntity<String> response = courseService.assignCourse(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User with id: 1 has been successfully assigned to course with id: 1", response.getBody());
        assertTrue(user.getCourseList().contains(course));
    }

    @Test
    public void assignCourseTest_UserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.assignCourse(1L, 1L));
    }

    @Test
    public void assignCourseTest_CourseDoesNotExist() {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.assignCourse(1L, 1L));
    }

    @Test
   public void removeCourseTest_CourseAndUserExists() {
        Course course = new Course();
        course.setCourseId(1L);

        User user = new User();
        user.setUserId(1L);
        user.setCourseList(new ArrayList<>(List.of(course)));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        ResponseEntity<String> response = courseService.removeCourse(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User is removed from this course !!", response.getBody());
        assertFalse(user.getCourseList().contains(course));
   }

   @Test
   public void removeCourseTest_UserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> courseService.removeCourse(1L, 1L));
   }

   @Test
   public void removeCourseTest_CourseDoesNotExist() {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.removeCourse(1L, 1L));
   }

   @Test
   public void removeCourseTest_CourseIsNotAssignedToUser(){
       Course course = new Course();
       course.setCourseId(1L);
       User user = new User();
       user.setUserId(1L);

       when(userRepository.findById(1L)).thenReturn(Optional.of(user));
       when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

       ResponseEntity<String> response = courseService.removeCourse(1L, 1L);

       assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
       assertFalse(user.getCourseList().contains(course));
       assertEquals("User is not assigned with this course !!", response.getBody());

   }

}
