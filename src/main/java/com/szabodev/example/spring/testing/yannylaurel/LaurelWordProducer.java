package com.szabodev.example.spring.testing.yannylaurel;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class LaurelWordProducer implements WordProducer {

    @Override
    public String sayWord() {
        return "Laurel";
    }
}
