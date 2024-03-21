package com.booleanuk.musicwebstorebackend.controller;

import com.booleanuk.musicwebstorebackend.model.Role;
import com.booleanuk.musicwebstorebackend.model.User;
import com.booleanuk.musicwebstorebackend.payload.request.LoginRequest;
import com.booleanuk.musicwebstorebackend.payload.request.SignupRequest;
import com.booleanuk.musicwebstorebackend.payload.response.ErrorResponse;
import com.booleanuk.musicwebstorebackend.payload.response.JwtResponse;
import com.booleanuk.musicwebstorebackend.payload.response.SuccessResponse;
import com.booleanuk.musicwebstorebackend.repository.RoleRepository;
import com.booleanuk.musicwebstorebackend.repository.UserRepository;
import com.booleanuk.musicwebstorebackend.security.jwt.JwtUtils;
import com.booleanuk.musicwebstorebackend.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // If using a salt for password use it here
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map((item) -> item.getAuthority())
                .collect(Collectors.toList());

        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        return ResponseEntity
                .ok(new JwtResponse(jwt, (user==null? "guest": user.getName()), userDetails.getId(), userDetails.getUsername(), roles.get(0)));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Email is already in use!"));
        }
        // Create a new user add salt here if using one
        User user = new User(signupRequest.getName(), signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));
        String strRole = signupRequest.getRole();

        Role role = roleRepository.findByTitle(strRole.toUpperCase()).orElseThrow(() -> new RuntimeException("Error: Role is not found"));

        user.setRole(role);
        userRepository.save(user);
        return ResponseEntity.ok((new SuccessResponse<>("User registered successfully")));
    }
}