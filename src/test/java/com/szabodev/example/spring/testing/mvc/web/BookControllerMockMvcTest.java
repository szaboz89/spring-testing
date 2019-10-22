package com.szabodev.example.spring.testing.mvc.web;

import com.szabodev.example.spring.testing.mvc.model.Book;
import com.szabodev.example.spring.testing.mvc.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerMockMvcTest {

    @Mock
    BookService bookService;

    @InjectMocks
    BookController controller;

    private List<Book> books = new ArrayList<>();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        books.add(Book.builder().build());
        given(bookService.findAll()).willReturn(books);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void bookList() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(view().name("book/books"));
    }
}
