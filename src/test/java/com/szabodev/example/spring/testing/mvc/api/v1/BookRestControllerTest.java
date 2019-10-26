package com.szabodev.example.spring.testing.mvc.api.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.szabodev.example.spring.testing.mvc.model.Book;
import com.szabodev.example.spring.testing.mvc.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
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
        book = Book.builder().id(1L).title("Test").bookType(Book.BookType.CRIME).createdDate(OffsetDateTime.now()).build();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(jackson2HttpMessageConverter()).build();
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

    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        objectMapper.registerModule(new JavaTimeModule());
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}
