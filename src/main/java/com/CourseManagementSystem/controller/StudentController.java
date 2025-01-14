package com.CourseManagementSystem.controller;

import com.CourseManagementSystem.dao.UserRepository;
import com.CourseManagementSystem.entities.*;
import com.CourseManagementSystem.service.UserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test")
    public String testing() {
        return "student";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationDTO registrationDTO) {
        return this.userService.registerUser(registrationDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JWTRequest jwtRequest) {
        return this.userService.login(jwtRequest);
    }

    @PutMapping("/edit-profile/{userId}")
    public ResponseEntity<String> editProfile(@PathVariable("userId") Long userId, @Valid @RequestBody EditProfileDTO editProfileDTO) {
        return this.userService.editProfile(userId, editProfileDTO);
    }

    @DeleteMapping(value = "/delete-profile/{userId}")
    public ResponseEntity<String> deleteProfile(@PathVariable("userId") Long userId) {
        return this.userService.deleteProfile(userId);
    }

    @GetMapping(value = "/get-users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @RateLimiter(name = "userRateLimiter", fallbackMethod = "userFallback")
    @GetMapping(value = "/get-user/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("userId") Long userId) {
        return this.userService.getUserById(userId);
    }

    public ResponseEntity<UserDTO> userFallback(String userId, Exception ex) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Dummy");
        userDTO.setEmail("dummy@gmail.com");
        userDTO.setAccountLocked(false);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

}
