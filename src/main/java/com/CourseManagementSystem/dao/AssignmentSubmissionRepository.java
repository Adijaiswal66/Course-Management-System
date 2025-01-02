package com.CourseManagementSystem.dao;

import com.CourseManagementSystem.entities.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {

    @Query("select as from AssignmentSubmission as where as.assignmentSubmissionName =:assignmentSubmissionName")
    public Optional<AssignmentSubmission> findByName(@Param("assignmentSubmissionName") String assignmentSubmissionName);
}
