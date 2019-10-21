package com.szabodev.example.spring.testing;

import com.szabodev.example.spring.testing.yannylaurel.HearingInterpreter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTestingApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(HearingInterpreter hearingInterpreter) {
        return args -> hearingInterpreter.whatIHeard();
    }
}
