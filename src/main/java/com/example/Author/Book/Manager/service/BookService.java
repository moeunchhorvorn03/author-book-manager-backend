package com.example.Author.Book.Manager.service;

import com.example.Author.Book.Manager.dto.BookDTO;
import com.example.Author.Book.Manager.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> findAll(BookDTO filter);

    Optional<Book> findById(Long id);

    void insert(Book book);

    void update(Book book);

    void delete(Long id);
}
