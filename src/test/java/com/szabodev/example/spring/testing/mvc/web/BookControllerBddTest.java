package com.szabodev.example.spring.testing.mvc.web;

import com.szabodev.example.spring.testing.mvc.model.Book;
import com.szabodev.example.spring.testing.mvc.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class BookControllerBddTest {

    @Mock
    BookService bookService;

    @Mock
    Model model;

    @InjectMocks
    BookController controller;

    private List<Book> books = new ArrayList<>();

    @BeforeEach
    void setUp() {
        books.add(Book.builder().build());
        given(bookService.findAll()).willReturn(books);
    }

    @Test
    void bookList() {
        // when
        String view = controller.bookList(model);

        // then
        then(bookService).should().findAll();
        then(model).should().addAttribute(anyString(), anyList());
        assertEquals("book/books", view);
    }
}
