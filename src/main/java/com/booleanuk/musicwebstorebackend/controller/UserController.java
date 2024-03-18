package com.booleanuk.musicwebstorebackend.controller;

import com.booleanuk.musicwebstorebackend.model.User;
import com.booleanuk.musicwebstorebackend.repository.UserRepository;
import com.booleanuk.musicwebstorebackend.response.ErrorResponse;
import com.booleanuk.musicwebstorebackend.response.Response;
import com.booleanuk.musicwebstorebackend.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Response<List<User>>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(this.userRepository.findAll()));
    }

    private User findUser(int id) {
        return userRepository.findById(id).orElse(null);
    }

    private boolean containsNull(User user) {
        return user.getUsername() == null || user.getEmail() == null || user.getPassword() == null;
    }
}


