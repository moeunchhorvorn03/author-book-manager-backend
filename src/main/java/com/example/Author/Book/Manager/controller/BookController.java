package com.example.Author.Book.Manager.controller;
import com.example.Author.Book.Manager.model.Book;
import com.example.Author.Book.Manager.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/create")
public class BookController {
    
    private final BookService bookService;

    @PostMapping
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

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Book book) {
        book.setId(id);
        bookService.update(book);
        return ResponseEntity.ok("Book updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok("The book has been deleted");
    }
}
