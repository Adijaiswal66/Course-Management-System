package com.CourseManagementSystem.service;

import com.CourseManagementSystem.Utils.ImageUtils;
import com.CourseManagementSystem.dao.AssignmentSubmissionRepository;
import com.CourseManagementSystem.entities.AssignmentSubmission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
public class AssignmentSubmissionService {

    @Autowired
    private AssignmentSubmissionRepository assignmentSubmissionRepository;


    public ResponseEntity<String> uploadFile(MultipartFile multipartFile) throws IOException {

        Optional<AssignmentSubmission> assignmentSubmission = this.assignmentSubmissionRepository.findByName(multipartFile.getOriginalFilename());

        if (assignmentSubmission.isEmpty()) {
            try {
                AssignmentSubmission data = new AssignmentSubmission();
                data.setAssignmentSubmissionName(multipartFile.getOriginalFilename());
                data.setType(multipartFile.getContentType());
                data.setAssignmentSubmissionDate(new Date());
                data.setImageData(ImageUtils.compressImage(multipartFile.getBytes()));
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


}
