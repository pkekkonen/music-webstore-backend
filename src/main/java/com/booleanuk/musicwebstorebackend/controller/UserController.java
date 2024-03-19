package com.booleanuk.musicwebstorebackend.controller;

import com.booleanuk.musicwebstorebackend.mapper.UserMapper;
import com.booleanuk.musicwebstorebackend.model.User;
import com.booleanuk.musicwebstorebackend.model.UserDTO;
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

    private UserMapper userMapper;
    @GetMapping
        public ResponseEntity<Response<List<UserDTO>>> getAllProducts() {
            List<UserDTO> allProducts = this.userRepository.findAll().stream().map(userMapper::toDto).toList();
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(allProducts));
        }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getUser(@PathVariable int id) {
        UserDTO user = userMapper.toDto(findUser(id));

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(user));
    }

    @PostMapping
    public ResponseEntity<Response<?>> createUser(@RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        if (containsNull(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Bad request"));
        }
        Response<UserDTO> res = new SuccessResponse<>(userMapper.toDto(userRepository.save(user)));
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
        User userToUpdate = findUser(id);
        if (userToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found"));
        }

        User userUpdated = userMapper.toEntity(userDTO);

        if(userUpdated.getName() != null ) {
            userToUpdate.setName(userUpdated.getName());
        }
        if(userUpdated.getEmail() != null ) {
            userToUpdate.setEmail(userUpdated.getEmail());
        }
        if(userUpdated.getPassword() != null ) {
            userToUpdate.setPassword(userUpdated.getPassword());
        }
        if(userUpdated.getRoles() != null ) {
            userToUpdate.setRoles(userUpdated.getRoles());
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(userMapper.toDto(userRepository.save(userToUpdate))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteUser(@PathVariable int id) {
        User userToDelete = findUser(id);
        if(userToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found"));
        }

        userRepository.delete(userToDelete);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(userMapper.toDto(userToDelete)));
    }

    private boolean containsNull(User user) {
        return (user.getName() == null || user.getEmail() == null || user.getPassword() == null);
    }

    private User findUser(int id) {
        return userRepository.findById(id).orElse(null);
    }

}
