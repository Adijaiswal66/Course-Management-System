package com.CourseManagementSystem.controller;

import com.CourseManagementSystem.entities.Assignment;
import com.CourseManagementSystem.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments-handler")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping(value = "/create-assignment")
    public ResponseEntity<String> createAssignment(@Valid @RequestBody Assignment assignment) {
        return this.assignmentService.createAssignment(assignment);
    }

    @GetMapping(value = "/view-assignments")
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        return this.assignmentService.getAllAssignments();
    }

    @PutMapping(value = "/edit-assignment/{assignmentID}")
    public ResponseEntity<String> editAssignment(@PathVariable("assignmentID") Long assignmentID, @RequestBody Assignment assignment) {
        return this.assignmentService.editAssignment(assignmentID, assignment);
    }
}
