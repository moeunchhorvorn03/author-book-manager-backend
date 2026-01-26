package com.example.Author.Book.Manager.service;

import com.example.Author.Book.Manager.dto.BookDTO;
import com.example.Author.Book.Manager.model.Book;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService {

    List<Book> findAll(BookDTO filter);

    Book findById(Long id);

    void insert(Book book);

    void update(Book book);

    void delete(Long id);
}
