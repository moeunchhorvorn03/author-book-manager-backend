package com.example.Author.Book.Manager.controller;

import com.example.Author.Book.Manager.model.Book;
import com.example.Author.Book.Manager.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/books")
public class BookInquiryDetailController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<Book> get(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
