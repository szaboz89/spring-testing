package com.szabodev.example.spring.testing.mvc.web;

import com.szabodev.example.spring.testing.mvc.model.Book;
import com.szabodev.example.spring.testing.mvc.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerMockMvcTest {

    @Mock
    BookService bookService;

    @InjectMocks
    BookController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void bookList() throws Exception {
        List<Book> books = Arrays.asList(Book.builder().build(), Book.builder().build());
        given(bookService.findAll()).willReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books))
                .andExpect(view().name(BookController.BOOK_BOOKS));
    }

    @Test
    void bookFormForNew() throws Exception {
        mockMvc.perform(get("/add-book"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(BookController.EDITED_BOOK_MODEL))
                .andExpect(view().name(BookController.BOOK_BOOK_FORM));
    }

    @Test
    void deleteBook() throws Exception {
        Book bookToDelete = Book.builder().id(1L).title("Test").build();
        given(bookService.findById(1L)).willReturn(Optional.of(bookToDelete));

        mockMvc.perform(get("/books/{id}/delete", bookToDelete.getId()))
                .andExpect(status().is3xxRedirection());

        then(bookService).should(times(1)).delete(bookToDelete);
    }

    @Test
    void bookFormEditWhenNotFound() throws Exception {
        Long id = 1L;
        given(bookService.findById(anyLong())).willReturn(Optional.empty());

        mockMvc.perform(get("/books/{id}/edit", id))
                .andExpect(status().is3xxRedirection());

        then(bookService).should(times(1)).findById(id);
    }

    @Test
    void bookFormEdit() throws Exception {
        Book bookToEdit = Book.builder().id(1L).title("Test").build();
        given(bookService.findById(bookToEdit.getId())).willReturn(Optional.of(bookToEdit));

        mockMvc.perform(get("/books/{id}/edit", bookToEdit.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(BookController.EDITED_BOOK_MODEL))
                .andExpect(model().attribute(BookController.EDITED_BOOK_MODEL, bookToEdit));

        then(bookService).should(times(1)).findById(bookToEdit.getId());
    }

    @Test
    void saveBook() throws Exception {
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        given(bookService.save(bookArgumentCaptor.capture())).willReturn(any());

        mockMvc.perform(post("/books")
                .param("title", "test")
                .param("bookType", "CRIME"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(BookController.REDIRECT_BOOKS));

        then(bookService).should(times(1)).save(any(Book.class));
        assertThat(bookArgumentCaptor.getValue().getTitle()).isEqualTo("test");
        assertThat(bookArgumentCaptor.getValue().getBookType()).isEqualTo(Book.BookType.CRIME);
    }
}
