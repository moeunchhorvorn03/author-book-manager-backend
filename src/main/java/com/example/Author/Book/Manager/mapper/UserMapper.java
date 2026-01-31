package com.example.Author.Book.Manager.mapper;

import com.example.Author.Book.Manager.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> findByEmail(@Param("email") String email);
    void insert(User user);
    void update(User user);
    Optional<User> findById(@Param("id") Long id);
}
