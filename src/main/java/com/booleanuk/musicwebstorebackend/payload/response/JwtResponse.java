package com.booleanuk.musicwebstorebackend.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String name;
    private String token;
    private String type;
    private int id;
    private String email;
    private String role;

    public JwtResponse(String token, String name, int id, String email, String role) {
        this.token = token;
        this.name = name;
        this.type = "Bearer";
        this.id = id;
        this.email = email;
        this.role = role;
    }
}
