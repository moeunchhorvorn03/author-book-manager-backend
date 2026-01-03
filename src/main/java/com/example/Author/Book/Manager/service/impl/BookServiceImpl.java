package com.example.Author.Book.Manager.service.impl;

import com.example.Author.Book.Manager.mapper.BookMapper;
import com.example.Author.Book.Manager.model.Book;
import com.example.Author.Book.Manager.service.BookService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public List<Book> findAll() {
        return bookMapper.findAll();
    }

    @Override
    public Book findById(Long id) {
        try {
            Book book = bookMapper.findById(id);
            if (book == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Book with ID " + id + " not found"
                );
            }
            return book;
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void insert(Book book) {
        if (book.getPublishedYear() == null || book.getPublishedYear().length() != 8) {
            throw new IllegalArgumentException("Published Year is invalid");
        }
        bookMapper.insert(book);
    }

    @Override
    public void update(Book book) {
        if (book.getPublishedYear() == null || book.getPublishedYear().length() != 8) {
            throw new IllegalArgumentException("Published Year is invalid");
        }
        bookMapper.update(book);
    }

    @Override
    public void delete(Long id) {
        bookMapper.delete(id);
    }
}
