package com.CourseManagementSystem.controller;

import com.CourseManagementSystem.entities.Assignment;
import com.CourseManagementSystem.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping(value = "/create-assignment")
    public ResponseEntity<String> createAssignment(@RequestBody Assignment assignment){
        return this.assignmentService.createAssignment(assignment);
    }
}
