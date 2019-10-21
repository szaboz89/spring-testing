package com.szabodev.example.spring.testing.yannylaurel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(classes = {BaseConfig.class, LaurelConfig.class})
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {BaseConfig.class, LaurelConfig.class})
class HearingInterpreterJUnit5LaurelTest {

    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    void whatIHeard() {
        String word = hearingInterpreter.whatIHeard();
        assertEquals("Laurel", word);
    }
}
