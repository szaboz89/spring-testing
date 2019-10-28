package com.szabodev.example.spring.testing.mvc.api.v1;

import com.szabodev.example.spring.testing.mvc.model.Book;
import com.szabodev.example.spring.testing.mvc.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(BookRestController.BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class BookRestController {

    static final String BASE_URL = "/api/v1/books";

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> books(@RequestParam(required = false) Book.BookType bookType) {
        return ResponseEntity.ok(bookType != null ? bookService.findByType(bookType) : bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        Optional<Book> byId = bookService.findById(id);
        if (byId.isPresent()) {
            return ResponseEntity.ok(byId.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
    }

    @PostMapping
    public ResponseEntity<Book> save(@RequestBody Book book) {
        log.info("Saving: {}", book);
        return ResponseEntity.ok(bookService.save(book));
    }
}
