package com.CourseManagementSystem.service;

import com.CourseManagementSystem.dao.UserRepository;
import com.CourseManagementSystem.entities.JWTRequest;
import com.CourseManagementSystem.entities.User;
import com.CourseManagementSystem.errors.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private final int MAX_ATTEMPTS = 5;
    private final long LOCK_TIME_DURATION = 15 * 60 * 1000;  // 15 minutes

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JWTService jwtService;

    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lockTimeCache = new ConcurrentHashMap<>();

    public void loginFailed(String email) {
        User user = this.userRepository.findByUserName(email);
        attemptsCache.put(email, attemptsCache.getOrDefault(email, 0) + 1);
        if (attemptsCache.get(email) >= MAX_ATTEMPTS) {
            user.setAccountLocked(true);
            this.userRepository.save(user);
            lockTimeCache.put(email, System.currentTimeMillis() + LOCK_TIME_DURATION);
        }
    }

    public void loginSucceeded(String email) {
        User user = this.userRepository.findByUserName(email);
        attemptsCache.remove(email);
        lockTimeCache.remove(email);
        user.setAccountLocked(false);
        this.userRepository.save(user);
    }

    public boolean isBlocked(String email) {
        User user = this.userRepository.findByUserName(email);

        if (!lockTimeCache.containsKey(email)) return false;

        if (lockTimeCache.get(email) < System.currentTimeMillis()) {
            lockTimeCache.remove(email); // Unlock user
            user.setAccountLocked(false);
            return false;
        }
        return true;
    }

    @Transactional
    public ResponseEntity<String> registerUser(User user) {

        Optional<User> existingUser = this.userRepository.findByEmail(user.getEmail());
        if (existingUser.isEmpty()) {
            try {
                user.setPassword(encoder.encode(user.getPassword()));
                user.setEmail(user.getEmail().toLowerCase());
                user.setAccountLocked(false);
                this.userRepository.save(user);
                return new ResponseEntity<>("User with email " + user.getEmail() + " is registered successfully !!", HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(user.getEmail() + " already exists !", HttpStatus.ALREADY_REPORTED);

    }

//    public ResponseEntity<String> login(JWTRequest jwtRequest) {
//
//        Authentication authenticate = this.manager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword()));
//
//        if (authenticate.isAuthenticated()) {
//            String token = jwtService.generateToken(jwtRequest.getEmail());
//            return new ResponseEntity<>(token, HttpStatus.OK);
//        }
//        return new ResponseEntity<>("User not found !!", HttpStatus.NOT_FOUND);
//    }

    public ResponseEntity<?> login(JWTRequest jwtRequest) {
        if (isBlocked(jwtRequest.getEmail())) {
            return new ResponseEntity<>("Your account is locked due to too many failed attempts. Please try again after some time!!", HttpStatus.FORBIDDEN);
        }
        try {
            // Authenticate the user
            Authentication authenticate = this.manager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword()));
            loginSucceeded(jwtRequest.getEmail());
            if (authenticate.isAuthenticated()) {
                // Generate JWT token
                String token = jwtService.generateToken(jwtRequest.getEmail());
                // Create HTTP-only cookie for the token
                ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", token)
                        .httpOnly(true)
                        .secure(true) // Ensures it's only sent over HTTPS
                        .path("/")
                        .maxAge(7 * 24 * 60 * 60) // Token expiry (7 days)
                        .build();

                // Return cookie and a success response
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                        .body("Login successful!");
            }
        } catch (Exception e) {
            loginFailed(jwtRequest.getEmail());
            System.out.println(e.getMessage());
        }
        // Handle authentication failure
        return new ResponseEntity<>("Invalid email or password.", HttpStatus.UNAUTHORIZED);
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
        if (existingUser.isPresent()) {
            this.userRepository.delete(existingUser.get());
            return new ResponseEntity<>("User with id: " + userId + " is deleted successfully!!", HttpStatus.OK
            );
        }
        return new ResponseEntity<>("User with id " + userId + " does not exist!!", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = this.userRepository.findAll();
        if (userList.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    public ResponseEntity<User> getUserById(Long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!!"));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
