package com.szabodev.example.spring.testing.mvc.api.v1;

import com.szabodev.example.spring.testing.mvc.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookRestControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testFindById() {
        Book[] books = restTemplate.getForObject(BookRestController.BASE_URL, Book[].class);
        assertThat(books).hasSize(3);
    }
}
