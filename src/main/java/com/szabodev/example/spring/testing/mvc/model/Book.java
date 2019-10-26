package com.szabodev.example.spring.testing.mvc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(value = EnumType.STRING)
    private BookType bookType;

    @CreationTimestamp
    @Column(updatable = false)
    private OffsetDateTime createdDate;

    public enum BookType {
        CRIME, DRAMA, ROMANCE;

        public static List<String> stringValues() {
            BookType[] values = BookType.values();
            List<String> stringValues = new ArrayList<>();
            for (BookType value : values) {
                stringValues.add(value.name());
            }
            return stringValues;
        }
    }
}
