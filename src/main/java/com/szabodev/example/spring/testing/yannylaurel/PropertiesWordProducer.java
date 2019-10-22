package com.szabodev.example.spring.testing.yannylaurel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("externalized")
@Primary
public class PropertiesWordProducer implements WordProducer {

    @Value("${say.word}")
    private String word;

    @Override
    public String sayWord() {
        return word;
    }
}
