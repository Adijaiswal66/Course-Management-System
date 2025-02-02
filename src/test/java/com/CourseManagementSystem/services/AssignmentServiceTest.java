package com.CourseManagementSystem.services;

import com.CourseManagementSystem.dao.AssignmentRepository;
import com.CourseManagementSystem.entities.Assignment;
import com.CourseManagementSystem.service.AssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @InjectMocks
    private AssignmentService assignmentService;

    private Assignment assignment = new Assignment();

    @BeforeEach
    void setUp() {
        assignment.setAssignmentID(1L);
        assignment.setAssignmentName("TestAssignment");
    }

    @Test
    public void createAssignmentTest_AssignmentExists() {
        when(assignmentRepository.findByAssignmentName("TestAssignment")).thenReturn(Optional.of(assignment));

        ResponseEntity<String> response = assignmentService.createAssignment(assignment);

        assertEquals("Assignment already exists!!", response.getBody());
        assertEquals(HttpStatus.ALREADY_REPORTED, response.getStatusCode());
    }

    @Test
    public void createAssignmentTest_AssignmentDoesNotExist() {
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        ResponseEntity<String> response = assignmentService.createAssignment(assignment);

        assertEquals("Assignment is registered successfully!!", response.getBody());
    }

    @Test
    public void getAllAssignmentsTest_AssignmentExists() {
        Assignment assignment1 = new Assignment();
        assignment1.setAssignmentID(2L);
        assignment1.setAssignmentName("TestAssignment1");

        when(assignmentRepository.findAll()).thenReturn(new ArrayList<>(List.of(assignment,assignment1)));

        ResponseEntity<List<Assignment>> response = assignmentService.getAllAssignments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals("TestAssignment", response.getBody().get(0).getAssignmentName());
        assertEquals("TestAssignment1", response.getBody().get(1).getAssignmentName());
    }

    @Test
    public void getAllAssignmentsTest_AssignmentDoesNotExist() {
        when(assignmentRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Assignment>> response = assignmentService.getAllAssignments();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void editAssignmentTest_AssignmentExists() {
        assignment.setAssignmentDescription("TestDescription");

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        ResponseEntity<String> response = assignmentService.editAssignment(1L, assignment);

        assertEquals("Assignment with id: 1 is updated successfully !!", response.getBody());
        assertEquals("TestDescription", assignment.getAssignmentDescription());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


}
