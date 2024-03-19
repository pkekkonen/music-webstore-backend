package com.booleanuk.musicwebstorebackend.mapper;

import com.booleanuk.musicwebstorebackend.model.UserDTO;
import com.booleanuk.musicwebstorebackend.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole() != null ? user.getRole().getTitle() : null
        );
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return user;
    }
}
