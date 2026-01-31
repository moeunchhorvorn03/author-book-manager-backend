package com.example.Author.Book.Manager.controller;

import com.example.Author.Book.Manager.model.Book;
import com.example.Author.Book.Manager.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/books")
public class BookInquiryDetail {

    private final BookService bookService;

    public BookInquiryDetail(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> get(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
