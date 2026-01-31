package com.example.Author.Book.Manager.controller;

import com.example.Author.Book.Manager.model.Book;
import com.example.Author.Book.Manager.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/update")
public class BookUpdate {

    private final BookService bookService;

    public BookUpdate(BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Book book) {
        book.setId(id);
        bookService.update(book);
        return ResponseEntity.ok("Book updated successfully");
    }
}
