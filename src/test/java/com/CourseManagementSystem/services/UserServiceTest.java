package com.CourseManagementSystem.services;

import com.CourseManagementSystem.dao.UserRepository;
import com.CourseManagementSystem.entities.EditProfileDTO;
import com.CourseManagementSystem.entities.User;
import com.CourseManagementSystem.entities.UserDTO;
import com.CourseManagementSystem.errors.ResourceNotFoundException;
import com.CourseManagementSystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    @Test
    public void getUserByIdTest() {
        User user = new User(1L, "Test", "Test@gmail.com", "password", false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Call the service method
        ResponseEntity<UserDTO> response = userService.getUserById(1L);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDTO userDTO = response.getBody();
        assert userDTO != null;
        assertEquals(1L, userDTO.getUserId());
        assertEquals("Test", userDTO.getName());
        assertEquals("Test@gmail.com", userDTO.getEmail());
        assertEquals(false, userDTO.getAccountLocked());
    }

    @Test
    public void getUserByIdTest_NoUserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
        assertEquals("User not found!!", exception.getMessage());
    }

    @Test
    public void getAllUsersTest() {
        User user = new User(1L, "Test", "Test@gmail.com", "password", false);
        User user1 = new User(2L, "Test1", "Test1@gmail.com", "password", false);
        when(userRepository.findAll()).thenReturn(new ArrayList<>(List.of(user, user1)));

        ResponseEntity<List<UserDTO>> response = userService.getAllUsers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals("Test", response.getBody().get(0).getName());
        assertEquals("Test1", response.getBody().get(1).getName());
    }

    @Test
    public void getAllUsersTest_NOT_FOUND() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<UserDTO>> response = userService.getAllUsers();

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteProfileTest_UserExists() {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        ResponseEntity<String> response = userService.deleteProfile(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User with id: 1 is deleted successfully!!", response.getBody());
    }

    @Test
    public void deleteProfileTest_UserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = userService.deleteProfile(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User with id 1 does not exist!!", response.getBody());
    }

    @Test
    public void editProfileTest_UserExist() {
        User user = new User(1L, "Test", "Test@gmail.com", "password", false);

        EditProfileDTO editProfileDTO = new EditProfileDTO();
        editProfileDTO.setName("Changed Test Name");
        editProfileDTO.setPassword("ChangedPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<String> response = userService.editProfile(1L, editProfileDTO);

        assertNotNull(response);
        assertEquals("Changed Test Name", user.getName());
        assertEquals("User with email: Test@gmail.com is updated successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void editProfileTest_UserDoesNotExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EditProfileDTO editProfileDTO = new EditProfileDTO();

        ResponseEntity<String> response = userService.editProfile(1L, editProfileDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found !!", response.getBody());
    }

}
