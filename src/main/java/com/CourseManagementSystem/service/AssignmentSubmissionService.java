package com.CourseManagementSystem.service;

import com.CourseManagementSystem.Utils.ImageUtils;
import com.CourseManagementSystem.dao.AssignmentRepository;
import com.CourseManagementSystem.dao.AssignmentSubmissionRepository;
import com.CourseManagementSystem.dao.UserRepository;
import com.CourseManagementSystem.entities.Assignment;
import com.CourseManagementSystem.entities.AssignmentSubmission;
import com.CourseManagementSystem.entities.User;
import com.CourseManagementSystem.errors.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
public class AssignmentSubmissionService {

    @Autowired
    private AssignmentSubmissionRepository assignmentSubmissionRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public ResponseEntity<String> uploadFile(@PathVariable("userId") Long userId, @PathVariable("assignmentID") Long assignmentID, MultipartFile multipartFile) throws IOException {
        Assignment assignment = this.assignmentRepository.findById(assignmentID).orElseThrow(() -> new ResourceNotFoundException("Assignment does not exist!!"));
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User does not exist!!"));
        Optional<AssignmentSubmission> assignmentSubmission = this.assignmentSubmissionRepository.findByName(multipartFile.getOriginalFilename());

        if (isAssignmentSubmitted(userId, assignmentID)) {
            return new ResponseEntity<>("User has already submitted the assignment  ", HttpStatus.ALREADY_REPORTED);
        }

        if (assignmentSubmission.isEmpty() && assignment != null && user != null) {
            try {
                AssignmentSubmission data = new AssignmentSubmission();
                data.setAssignmentSubmissionName(multipartFile.getOriginalFilename());
                data.setType(multipartFile.getContentType());
                data.setAssignmentSubmissionDate(new Date());
                data.setImageData(ImageUtils.compressImage(multipartFile.getBytes()));
                data.setUser(user);
                data.setAssignment(assignment);
                this.assignmentSubmissionRepository.save(data);
                return new ResponseEntity<>("File uploaded successfully: " + multipartFile.getOriginalFilename(), HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return new ResponseEntity<>("File already exists with this name", HttpStatus.ALREADY_REPORTED);
    }

    public ResponseEntity<?> downloadFile(Long assignmentSubmissionId) {
        Optional<AssignmentSubmission> downloadedFile = this.assignmentSubmissionRepository.findById(assignmentSubmissionId);
        if (downloadedFile.isPresent()) {
            byte[] bytesImage = ImageUtils.decompressImage(downloadedFile.get().getImageData());
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(bytesImage);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public boolean isAssignmentSubmitted(Long userId, Long assignmentID) {
        return assignmentSubmissionRepository.existsByUser_userIdAndAssignment_assignmentID(userId, assignmentID);
    }


}
