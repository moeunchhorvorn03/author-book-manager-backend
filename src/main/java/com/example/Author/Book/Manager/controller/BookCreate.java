package com.example.Author.Book.Manager.controller;

import com.example.Author.Book.Manager.model.Book;
import com.example.Author.Book.Manager.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookCreate {

    private final BookService bookService;

    public BookCreate(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Book book) {
        try {
            bookService.insert(book);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Book created successfully");
        } catch (Exception error) {
            throw new Error(error);
        }
    }
}
