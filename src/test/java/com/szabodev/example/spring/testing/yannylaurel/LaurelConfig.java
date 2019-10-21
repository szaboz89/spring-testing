package com.szabodev.example.spring.testing.yannylaurel;

import org.springframework.context.annotation.Bean;

class LaurelConfig {

    @Bean
    LaurelWordProducer laurelWordProducer() {
        return new LaurelWordProducer();
    }
}
