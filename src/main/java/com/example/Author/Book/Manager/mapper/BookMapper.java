package com.example.Author.Book.Manager.mapper;

import com.example.Author.Book.Manager.model.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {
    List<Book> findAll(String category);
    List<Book> findByAuthorId(Long authorId);
    Book findById(Long id);
    void insert(Book book);
    void update(Book book);
    void delete(Long id);
}