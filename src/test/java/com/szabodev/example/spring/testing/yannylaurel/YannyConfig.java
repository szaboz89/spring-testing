package com.szabodev.example.spring.testing.yannylaurel;

import org.springframework.context.annotation.Bean;

class YannyConfig {

    @Bean
    YannyWordProducer laurelWordProducer() {
        return new YannyWordProducer();
    }
}
