package com.CourseManagementSystem.dao;

import com.CourseManagementSystem.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select c from Course c where c.courseName =:courseName ")
    public Optional<Course> findByCourseName(@Param("courseName") String courseName);

}
