package com.example.Author.Book.Manager.controller;

import com.example.Author.Book.Manager.dto.AuthResponse;
import com.example.Author.Book.Manager.dto.LoginRequest;
import com.example.Author.Book.Manager.dto.RegisterRequest;
import com.example.Author.Book.Manager.model.User;
import com.example.Author.Book.Manager.service.UserService;
import com.example.Author.Book.Manager.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setRole(request.getRole());
            // failed_login_attempts will default to 0 in the model

            User savedUser = userService.register(user);

            String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole());

            AuthResponse response = new AuthResponse(
                token,
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getUsername()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = (User) authentication.getPrincipal();

            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

            AuthResponse response = new AuthResponse(
                    token,
                    user.getEmail(),
                    user.getRole(),
                    user.getUsername()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}
