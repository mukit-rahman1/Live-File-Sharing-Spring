package com.wolvie.fileShare.controller;

import com.wolvie.fileShare.service.JWTService;
import org.springframework.http.HttpHeaders;
import jakarta.servlet.http.HttpServletResponse;

import com.wolvie.fileShare.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import java.util.Map;

import com.wolvie.fileShare.model.Users;
import com.wolvie.fileShare.model.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JWTService jwtService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        // auto-generate JWT
        UserDetails userDetails = userRepository.findByUsername(user.getUsername()).get();
        String jwt = jwtService.generateToken(userDetails);
        return ResponseEntity.ok().body(Map.of("token", jwt));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails user = userRepository.findByUsername(request.getUsername()).get();
        String jwt = jwtService.generateToken(user);

        return ResponseEntity.ok().body(Map.of("token", jwt));
    }

    

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        //clear jwt local
        response.addHeader("jwt", "");

        return ResponseEntity.ok().body(Map.of("message", "Logged out"));
    }
}