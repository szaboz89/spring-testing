package com.szabodev.example.spring.testing.mvc.api.v1;

import com.szabodev.example.spring.testing.mvc.model.Book;
import com.szabodev.example.spring.testing.mvc.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookRestControllerTest {

    @Mock
    BookService bookService;

    @InjectMocks
    BookRestController controller;

    private MockMvc mockMvc;

    private Book book;

    @BeforeEach
    void setUp() {
        book = Book.builder().id(1L).title("Test").bookType(Book.BookType.CRIME).build();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testBooks() throws Exception {
        given(bookService.findAll()).willReturn(Arrays.asList(book, Book.builder().title("Test2").bookType(Book.BookType.DRAMA).build()));

        mockMvc.perform(get(BookRestController.BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(book.getId().intValue())))
                .andReturn();
    }

    @Test
    void testFindById() throws Exception {
        given(bookService.findById(1L)).willReturn(Optional.of(book));

        MvcResult mvcResult = mockMvc.perform(get(BookRestController.BASE_URL + "/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(book.getId().intValue())))
                .andExpect(jsonPath("$.title", is(book.getTitle())))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        given(bookService.findById(1L)).willReturn(Optional.empty());

        MvcResult mvcResult = mockMvc.perform(get(BookRestController.BASE_URL + "/{id}", book.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}
