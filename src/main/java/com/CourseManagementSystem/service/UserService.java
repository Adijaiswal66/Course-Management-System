package com.CourseManagementSystem.service;

import com.CourseManagementSystem.dao.UserRepository;
import com.CourseManagementSystem.entities.JWTRequest;
import com.CourseManagementSystem.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JWTService jwtService;

    public ResponseEntity<String> registerUser(User user) {

        Optional<User> existingUser = this.userRepository.findByEmail(user.getEmail());
        if (existingUser.isEmpty()) {
            try {
                user.setPassword(encoder.encode(user.getPassword()));
                System.out.println(user);
                this.userRepository.save(user);
                return new ResponseEntity<>("User with email " + user.getEmail() + " is registered successfully !!", HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(user.getEmail() + " already exists !", HttpStatus.ALREADY_REPORTED);

    }

    public ResponseEntity<String> login(JWTRequest jwtRequest) {
        System.out.println("JWT Request: " + jwtRequest);

        Authentication authenticate = this.manager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword()));

        if (authenticate.isAuthenticated()) {
            String token = jwtService.generateToken(jwtRequest.getEmail());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found !!", HttpStatus.NOT_FOUND);


    }


    public ResponseEntity<String> editProfile(Long userId, User user) {
        Optional<User> existingUser = this.userRepository.findById(userId);
        if (existingUser.isPresent()) {
            try {
                existingUser.map(u -> {
                    u.setName(user.getName());
                    u.setPassword(encoder.encode(user.getPassword()));
                    return this.userRepository.save(u);
                });

                return new ResponseEntity<>("User with email: " + existingUser.get().getEmail() + " is updated successfully", HttpStatus.OK);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        return new ResponseEntity<>("User not found !!", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteProfile(Long userId) {
        Optional<User> existingUser = this.userRepository.findById(userId);
        if (existingUser.isPresent()){
            this.userRepository.delete(existingUser.get());
            return new ResponseEntity<>("User with id: " + userId+" is deleted successfully!!",HttpStatus.OK
            );
        }
        return new ResponseEntity<>("User with id " + userId + " does not exist!!", HttpStatus.NOT_FOUND);
    }
}
