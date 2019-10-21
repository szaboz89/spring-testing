package com.szabodev.example.spring.testing.yannylaurel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HearingInterpreter {

    private final WordProducer wordProducer;

    @Autowired
    public HearingInterpreter(WordProducer wordProducer) {
        this.wordProducer = wordProducer;
    }

    public String whatIHeard() {
        String word = wordProducer.sayWord();
        System.out.println(word);
        return word;
    }
}
