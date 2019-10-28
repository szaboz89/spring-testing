package com.szabodev.example.spring.testing.mvc.service;

import com.szabodev.example.spring.testing.mvc.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book save(Book book);

    Optional<Book> findById(Long id);

    List<Book> findAll();

    void delete(Book book);

    List<Book> findByType(Book.BookType bookType);
}
