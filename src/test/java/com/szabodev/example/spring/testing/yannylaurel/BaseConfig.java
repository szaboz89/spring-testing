package com.szabodev.example.spring.testing.yannylaurel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

class BaseConfig {

    @Bean
    @Primary
    HearingInterpreter hearingInterpreter(WordProducer wordProducer) {
        return new HearingInterpreter(wordProducer);
    }
}
