package com.example.Author.Book.Manager.controller;

import com.example.Author.Book.Manager.model.Book;
import com.example.Author.Book.Manager.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookInquiry {

    private final BookService bookService;

    public BookInquiry(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public List<Book> searchBook(@RequestBody Book filter) {
        String category = filter.getCategory();
        return bookService.findAll(category);
    }
}
