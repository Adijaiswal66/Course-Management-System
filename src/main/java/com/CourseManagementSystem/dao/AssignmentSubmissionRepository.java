package com.CourseManagementSystem.dao;

import com.CourseManagementSystem.entities.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {

    @Query("select as from AssignmentSubmission as where as.assignmentSubmissionName =:assignmentSubmissionName")
    public Optional<AssignmentSubmission> findByName(@Param("assignmentSubmissionName") String assignmentSubmissionName);

    // Custom query to check if the submission exists for a given user and assignment
//    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AssignmentSubmission a WHERE a.userId = :userId AND a.assignmentID = :assignmentID")
//    boolean existsByUserIdAndAssignmentId(@Param("userId") Long userId, @Param("assignmentID") Long assignmentID);

    boolean existsByUser_userIdAndAssignment_assignmentID(Long userId, Long assignmentID);

}
