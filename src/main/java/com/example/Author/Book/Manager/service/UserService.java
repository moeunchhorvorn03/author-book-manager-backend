package com.example.Author.Book.Manager.service;

import com.example.Author.Book.Manager.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
    User register(User user);
    Optional<User> findById(Long id);
}
