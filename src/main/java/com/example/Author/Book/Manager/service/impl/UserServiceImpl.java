package com.example.Author.Book.Manager.service.impl;

import com.example.Author.Book.Manager.mapper.UserMapper;
import com.example.Author.Book.Manager.model.User;
import com.example.Author.Book.Manager.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user = userMapper.findByEmail(email);
        user.ifPresent(u -> {
            if (u.getFailed_login_attempts() == 0 && u.getId() != null) {
                // Default to 0 if not set
                u.setFailed_login_attempts(0);
            }
        });
        return user;
    }

    @Override
    public User register(User user) {
        // Check if user already exists
        if (userMapper.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        userMapper.insert(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userMapper.findById(id);
    }
}
