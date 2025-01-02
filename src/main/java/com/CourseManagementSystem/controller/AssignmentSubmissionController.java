package com.CourseManagementSystem.controller;

import com.CourseManagementSystem.service.AssignmentSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/assignment-submission")
public class AssignmentSubmissionController {

    @Autowired
    private AssignmentSubmissionService assignmentSubmissionService;

    @PostMapping(value = "/upload/{userId}/{assignmentID}")
    public ResponseEntity<String> uploadFile(@PathVariable("userId") Long userId, @PathVariable("assignmentID") Long assignmentID, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        return this.assignmentSubmissionService.uploadFile(userId, assignmentID, multipartFile);
    }

    @GetMapping(value = "/download/{assignmentSubmissionId}")
    public ResponseEntity<?> downloadFile(@PathVariable("assignmentSubmissionId") Long assignmentSubmissionId) {
        return this.assignmentSubmissionService.downloadFile(assignmentSubmissionId);
    }
}
