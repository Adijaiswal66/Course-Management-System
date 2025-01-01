package com.CourseManagementSystem.dao;

import com.CourseManagementSystem.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query("select a from Assignment a where a.assignmentName =:assignmentName")
    public Optional<Assignment> findByAssignmentName(@Param("assignmentName") String assignmentName);
}
