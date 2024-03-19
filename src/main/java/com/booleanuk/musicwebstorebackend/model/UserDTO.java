package com.booleanuk.musicwebstorebackend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDTO {

    private int id;
    private String name;
    private String email;
    private String password;
    private String title;
    private List<String> roles;

    public UserDTO(int id, String name, String email, String password, String title, List<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.title = title;
        this.roles = roles;
    }

    public UserDTO() {

    }
}
