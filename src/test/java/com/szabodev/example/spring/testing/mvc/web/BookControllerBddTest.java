package com.szabodev.example.spring.testing.mvc.web;

import com.szabodev.example.spring.testing.mvc.model.Book;
import com.szabodev.example.spring.testing.mvc.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BookControllerBddTest {

    @Mock
    BookService bookService;

    @Mock
    Model model;

    @InjectMocks
    BookController controller;

    @Test
    void bookList() {
        // given
        List<Book> books = Arrays.asList(Book.builder().build(), Book.builder().build());
        given(bookService.findAll()).willReturn(books);

        // when
        String view = controller.bookList(model);

        // then
        then(bookService).should(times(1)).findAll();
        then(model).should().addAttribute("books", books);
        assertEquals(BookController.BOOK_BOOKS, view);
    }

    @Test
    void bookFormForNew() {
        // when
        String view = controller.bookFormForNew(model);

        // then
        assertThat(view).isEqualTo(BookController.BOOK_BOOK_FORM);
        then(model).should().addAttribute(BookController.EDITED_BOOK_MODEL, Book.builder().build());
    }

    @Test
    void deleteBook() {
        // given
        Book bookToDelete = Book.builder().id(1L).title("Test").build();
        given(bookService.findById(1L)).willReturn(Optional.of(bookToDelete));

        // when
        String view = controller.deleteBook(bookToDelete.getId());

        // then
        assertThat(view).isEqualTo(BookController.REDIRECT_BOOKS);
        then(bookService).should(times(1)).delete(bookToDelete);
    }

    @Test
    void bookFormEditWhenNotFound() {
        // given
        Long id = 1L;
        given(bookService.findById(anyLong())).willReturn(Optional.empty());

        // when
        String view = controller.bookFormForEdit(model, id);

        // then
        assertThat(view).isEqualTo(BookController.REDIRECT_BOOKS);
        then(bookService).should(times(1)).findById(id);
    }

    @Test
    void bookFormEdit() {
        // given
        Book bookToEdit = Book.builder().id(1L).title("Test").build();
        given(bookService.findById(bookToEdit.getId())).willReturn(Optional.of(bookToEdit));

        // when
        String view = controller.bookFormForEdit(model, bookToEdit.getId());

        // then
        assertThat(view).isEqualTo(BookController.BOOK_BOOK_FORM);
        then(model).should().addAttribute(BookController.EDITED_BOOK_MODEL, bookToEdit);
        then(bookService).should(times(1)).findById(bookToEdit.getId());
    }

    @Test
    void saveBook() {
        // given
        Book bookToSave = Book.builder().title("test").bookType(Book.BookType.CRIME).build();
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        given(bookService.save(bookArgumentCaptor.capture())).willReturn(any());

        // when
        String view = controller.saveBook(bookToSave);

        // then
        assertThat(view).isEqualTo(BookController.REDIRECT_BOOKS);
        then(bookService).should(times(1)).save(any(Book.class));
        assertThat(bookArgumentCaptor.getValue().getTitle()).isEqualTo("test");
        assertThat(bookArgumentCaptor.getValue().getBookType()).isEqualTo(Book.BookType.CRIME);
    }
}
