package com.booleanuk.musicwebstorebackend.mapper;

import com.booleanuk.musicwebstorebackend.model.UserDTO;
import com.booleanuk.musicwebstorebackend.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());

        if (!user.getRoles().isEmpty()) {
            userDTO.setTitle(user.getRoles().get(0));
        }

        userDTO.setRoles(user.getRoles());

        return new UserDTO(
                userDTO.getId(),
                userDTO.getName(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getTitle(),
                userDTO.getRoles()
        );
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRoles(userDTO.getRoles());
        return user;
    }
}
