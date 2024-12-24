package com.CourseManagementSystem.service;

import com.CourseManagementSystem.dao.UserRepository;
import com.CourseManagementSystem.entities.CustomerUserDetails;
import com.CourseManagementSystem.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User existingUser = this.userRepository.findByUserName(email);

        if (existingUser != null) {
            return new CustomerUserDetails(existingUser);
        } else throw new UsernameNotFoundException("User not found!!");


    }
}
