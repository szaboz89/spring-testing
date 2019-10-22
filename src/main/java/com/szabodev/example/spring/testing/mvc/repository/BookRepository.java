package com.szabodev.example.spring.testing.mvc.repository;

import com.szabodev.example.spring.testing.mvc.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
