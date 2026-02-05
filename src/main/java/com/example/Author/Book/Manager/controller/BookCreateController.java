package com.example.Author.Book.Manager.controller;

import com.example.Author.Book.Manager.model.Book;
import com.example.Author.Book.Manager.service.BookService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookCreateController {
    
    private final BookService bookService;

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
