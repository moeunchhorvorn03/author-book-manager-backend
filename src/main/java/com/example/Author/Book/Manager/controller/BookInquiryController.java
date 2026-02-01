package com.example.Author.Book.Manager.controller;

import com.example.Author.Book.Manager.dto.BookDTO;
import com.example.Author.Book.Manager.model.Book;
import com.example.Author.Book.Manager.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookInquiryController {

    private final BookService bookService;

    public BookInquiryController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/filter")
    public List<Book> searchBook(@RequestBody BookDTO filter) {
        return bookService.findAll(filter);
    }
}
