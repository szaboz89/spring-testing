package com.szabodev.example.spring.testing.yannylaurel;

import org.springframework.stereotype.Component;

@Component
public class YannyWordProducer implements WordProducer {

    @Override
    public String sayWord() {
        return "Yanny";
    }
}
