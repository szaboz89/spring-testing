package com.szabodev.example.spring.testing.mvc.service;

import com.szabodev.example.spring.testing.mvc.model.Book;
import com.szabodev.example.spring.testing.mvc.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookServiceImpl bookService;

    @Test
    void save() {
        // given
        Book book = Book.builder().title("Test").bookType(Book.BookType.ROMANCE).build();

        // when
        bookService.save(book);

        // then
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void findById() {
        // given
        Book book = Book.builder().id(1L).title("Test").bookType(Book.BookType.ROMANCE).build();
        given(bookRepository.findById(book.getId())).willReturn(Optional.of(book));

        // when
        Book foundBook = bookService.findById(book.getId()).orElse(null);

        // then
        assertThat(foundBook).isNotNull();
        then(bookRepository).should(times(1)).findById(anyLong());
        then(bookRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findAll() {
        // given
        Book book1 = Book.builder().title("Test1").bookType(Book.BookType.ROMANCE).build();
        Book book2 = Book.builder().title("Test2").bookType(Book.BookType.CRIME).build();
        given(bookRepository.findAll()).willReturn(Arrays.asList(book1, book2));

        // when
        List<Book> books = bookService.findAll();

        // then
        then(bookRepository).should(times(1)).findAll();
        assertThat(books).hasSize(2);
    }

    @Test
    void delete() {
        // given
        Book book = Book.builder().id(1L).title("Test").bookType(Book.BookType.ROMANCE).build();

        // when
        bookService.delete(book);

        // then
        then(bookRepository).should().delete(book);
    }

    @Test
    void deleteNull() {
        // when
        bookService.delete(null);

        // then
        then(bookRepository).should(never()).delete(any());
    }

    @Test
    void findByType() {
        // given
        Book.BookType bookTypeToSearch = Book.BookType.CRIME;
        Book book1 = Book.builder().title("Test1").bookType(bookTypeToSearch).build();
        Book book2 = Book.builder().title("Test2").bookType(bookTypeToSearch).build();
        given(bookRepository.findByBookType(bookTypeToSearch)).willReturn(Arrays.asList(book1, book2));

        // when
        List<Book> books = bookService.findByType(bookTypeToSearch);

        // then
        then(bookRepository).should(times(1)).findByBookType(bookTypeToSearch);
        assertThat(books).hasSize(2);
    }
}
