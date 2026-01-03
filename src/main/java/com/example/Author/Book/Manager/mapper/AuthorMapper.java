package com.example.Author.Book.Manager.mapper;

import com.example.Author.Book.Manager.model.Author;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthorMapper {
    List<Author> findAll();
    Author findById(Long id);
    int insert(Author author);
    int update(Author author);
    int delete(Long id);
}
