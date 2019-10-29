package com.szabodev.example.spring.testing.mvc.api.v1;

import com.szabodev.example.spring.testing.mvc.model.Book;
import com.szabodev.example.spring.testing.mvc.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
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
                .andDo(
                        document("v1/books/id",
                                pathParameters(
                                        parameterWithName("id").description("Id of desired book to get.")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("Id of the book"),
                                        fieldWithPath("title").description("Title of the book"),
                                        fieldWithPath("bookType").description("Type of the book"),
                                        fieldWithPath("createdDate").description("Date Created")
                                )
                        )
                )
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testBooksByParam() throws Exception {
        Book book2 = Book.builder().id(2L).title("Test2").bookType(Book.BookType.CRIME).createdDate(OffsetDateTime.now()).build();
        given(bookService.findByType(Book.BookType.CRIME)).willReturn(Arrays.asList(book, book2));

        mockMvc.perform(get(BookRestController.BASE_URL).param("bookType", "CRIME"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(book.getId().intValue())))
                .andDo(
                        document("v1/books",
                                requestParameters(
                                        parameterWithName("bookType").description("Type of the desired books to get")
                                ),
                                responseFields(
                                        fieldWithPath("[]").description("An array of books"))
                                        .andWithPrefix("[].",
                                                fieldWithPath("id").description("Id of the book"),
                                                fieldWithPath("title").description("Title of the book"),
                                                fieldWithPath("bookType").description("Type of the book"),
                                                fieldWithPath("createdDate").description("Date Created")
                                        )
                        )
                );
    }

    @Test
    void testSave() throws Exception {
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        given(bookService.save(bookArgumentCaptor.capture())).willReturn(book);

        mockMvc.perform(post(BookRestController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": 1, \"title\": \"Test\", \"bookType\": \"CRIME\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test")));

        then(bookService).should(times(1)).save(any(Book.class));
        assertThat(bookArgumentCaptor.getValue().getId()).isEqualTo(1L);
        assertThat(bookArgumentCaptor.getValue().getTitle()).isEqualTo("Test");
        assertThat(bookArgumentCaptor.getValue().getBookType()).isEqualTo(Book.BookType.CRIME);
    }
}
