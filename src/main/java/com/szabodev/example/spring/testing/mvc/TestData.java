package com.szabodev.example.spring.testing.mvc;

import com.szabodev.example.spring.testing.mvc.model.Book;
import com.szabodev.example.spring.testing.mvc.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestData {

    private final BookService bookService;

    @PostConstruct
    public void initTestData() {
        bookService.save(Book.builder().title("book1").bookType(Book.BookType.CRIME).build());
        bookService.save(Book.builder().title("book2").bookType(Book.BookType.DRAMA).build());
        bookService.save(Book.builder().title("book3").bookType(Book.BookType.ROMANCE).build());
    }
}
