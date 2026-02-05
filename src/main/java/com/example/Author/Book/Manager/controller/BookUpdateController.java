package com.example.Author.Book.Manager.controller;
import com.example.Author.Book.Manager.model.Book;
import com.example.Author.Book.Manager.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/update")
@RequiredArgsConstructor
public class BookUpdateController {

    private final BookService bookService;

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Book book) {
        book.setId(id);
        bookService.update(book);
        return ResponseEntity.ok("Book updated successfully");
    }
}
