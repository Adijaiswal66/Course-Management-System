package com.CourseManagementSystem.dao;

import com.CourseManagementSystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email =:email")
    public Optional<User> findByEmail(@Param("email") String email);

    @Query("select u from User u where u.email =:email")
    public User findByUserName(String email);


}
