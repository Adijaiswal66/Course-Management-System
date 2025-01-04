package com.CourseManagementSystem.controller;

import com.CourseManagementSystem.dao.UserRepository;
import com.CourseManagementSystem.entities.JWTRequest;
import com.CourseManagementSystem.entities.User;
import com.CourseManagementSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        return this.userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JWTRequest jwtRequest) {
        return this.userService.login(jwtRequest);
    }

    @PutMapping("/edit-profile/{userId}")
    public ResponseEntity<String> editProfile(@PathVariable("userId") Long userId, @RequestBody User user){
        return this.userService.editProfile(userId,user);
    }

    @DeleteMapping(value = "/delete-profile/{userId}")
    public ResponseEntity<String> deleteProfile(@PathVariable("userId") Long userId){
        return this.userService.deleteProfile(userId);
    }

    @GetMapping(value = "/get-users")
    public ResponseEntity<List<User>> getAllUsers(){
        return this.userService.getAllUsers();
    }

    @GetMapping(value = "/get-user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Long userId){
        return this.userService.getUserById(userId);
    }

}
