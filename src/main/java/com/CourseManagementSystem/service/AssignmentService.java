package com.CourseManagementSystem.service;

import com.CourseManagementSystem.dao.AssignmentRepository;
import com.CourseManagementSystem.entities.Assignment;
import com.CourseManagementSystem.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public ResponseEntity<String> createAssignment(Assignment assignments) {
        Optional<Assignment> assignment = this.assignmentRepository.findByAssignmentName(assignments.getAssignmentName());
        if (assignment.isEmpty()) {
            System.out.println(assignments);
            this.assignmentRepository.save(assignments);
            return new ResponseEntity<>("Assignment is registered successfully!!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Assignment already exists!!", HttpStatus.ALREADY_REPORTED);
    }

    public ResponseEntity<List<Assignment>> getAllAssignments() {
        List<Assignment> assignmentList = this.assignmentRepository.findAll();
        if (assignmentList.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(assignmentList, HttpStatus.OK);
    }

    public ResponseEntity<String> editAssignment(Long assignmentID, Assignment assignment) {
        Assignment existingAssignment = this.assignmentRepository.findById(assignmentID).orElseThrow(() -> new ResourceNotFoundException("Assignment not found!!"));
        existingAssignment.setAssignmentDescription(assignment.getAssignmentDescription());
        existingAssignment.setAssignmentDueDate(assignment.getAssignmentDueDate());
        this.assignmentRepository.save(existingAssignment);
        return new ResponseEntity<>("Assignment with id: " + assignmentID + " is updated successfully !!", HttpStatus.OK);
    }
}
