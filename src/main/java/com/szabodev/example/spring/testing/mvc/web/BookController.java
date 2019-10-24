package com.szabodev.example.spring.testing.mvc.web;

import com.szabodev.example.spring.testing.mvc.model.Book;
import com.szabodev.example.spring.testing.mvc.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {

    static final String BOOK_BOOKS = "book/books";
    static final String BOOK_BOOK_FORM = "book/book-form";
    static final String REDIRECT_BOOKS = "redirect:/books";
    static final String EDITED_BOOK_MODEL = "editedBook";

    private final BookService bookService;

    @GetMapping({"/", "/books"})
    public String bookList(Model model) {
        model.addAttribute("books", bookService.findAll());
        return BOOK_BOOKS;
    }

    @GetMapping("/add-book")
    public String bookFormForNew(Model model) {
        model.addAttribute(EDITED_BOOK_MODEL, Book.builder().build());
        addStaticVariables(model);
        return BOOK_BOOK_FORM;
    }

    @GetMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookService.findById(id).ifPresent(book -> {
            log.info("Delete book: {}", book);
            bookService.delete(book);
        });
        return REDIRECT_BOOKS;
    }

    @GetMapping("/books/{id}/edit")
    public String bookFormForEdit(Model model, @PathVariable Long id) {
        Optional<Book> byId = bookService.findById(id);
        if (byId.isPresent()) {
            model.addAttribute(EDITED_BOOK_MODEL, byId.get());
            addStaticVariables(model);
            return BOOK_BOOK_FORM;
        } else {
            return REDIRECT_BOOKS;
        }
    }

    @PostMapping("/books")
    public String saveBook(@ModelAttribute(EDITED_BOOK_MODEL) Book book) {
        Book saved = bookService.save(book);
        log.info("Book saved: {}", saved);
        return REDIRECT_BOOKS;
    }

    private void addStaticVariables(Model model) {
        model.addAttribute("bookTypeValues", Book.BookType.stringValues());
    }
}
