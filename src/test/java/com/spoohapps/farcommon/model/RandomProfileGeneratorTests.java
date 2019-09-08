package com.spoohapps.farcommon.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RandomProfileGeneratorTests {

    private ProfileGenerator randomProfileGenerator;

    @BeforeAll
    public void setup() {
        randomProfileGenerator = new RandomProfileGenerator();
    }

    @Test
    public void shouldGenerateValidId() {

        Profile p = randomProfileGenerator.generate();

    }

}
