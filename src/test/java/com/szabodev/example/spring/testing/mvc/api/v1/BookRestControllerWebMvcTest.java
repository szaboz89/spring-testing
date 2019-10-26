package com.szabodev.example.spring.testing.mvc.api.v1;

import com.szabodev.example.spring.testing.mvc.model.Book;
import com.szabodev.example.spring.testing.mvc.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookRestController.class)
class BookRestControllerWebMvcTest {

    @MockBean
    BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    private Book book;

    @BeforeEach
    void setUp() {
        book = Book.builder().id(1L).title("Test").bookType(Book.BookType.CRIME).createdDate(OffsetDateTime.now()).build();
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
}
