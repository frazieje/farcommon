package com.spoohapps.farcommon.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WhenLoadingSimpleConfigTests {

    private TestConfigContract testConfig;

    private final String expectedName = "some name";
    private final String expectedDescription = "some description";
    private final int expectedInt = 12;
    private final Integer expectedInteger = 24;
    private final boolean expectedBool = true;
    private final Boolean expectedBoolean = Boolean.FALSE;

    @BeforeAll
    public void setup() {
        testConfig = new ConfigBuilder<>(TestConfigContract.class)
                .apply(new String[]{
                        "-n", expectedName,
                        "-desc", expectedDescription,
                        "-c", String.valueOf(expectedInt),
                        "-s", expectedInteger.toString(),
                        "-correct", String.valueOf(expectedBool),
                        "-done", String.valueOf(expectedBoolean),
                })
                .build();
    }

    @Test
    public void shouldConfigureString() {
        assertEquals(expectedName,  testConfig.name());
    }

    @Test
    public void shouldConfigureStringWithLongFlag() {
        assertEquals(expectedDescription, testConfig.description());
    }

    @Test
    public void shouldConfigurePrimitiveInteger() {
        assertEquals(expectedInt, testConfig.count());
    }

    @Test
    public void shouldConfigureBoxedInteger() {
        assertEquals(expectedInteger, testConfig.size());
    }

    @Test
    public void shouldConfigurePrimitiveBoolean() {
        assertEquals(expectedBool, testConfig.isCorrect());
    }

    @Test
    public void shoudlConfigureBoxedBoolean() {
        assertEquals(expectedBoolean, testConfig.isDone());
    }

}

interface TestConfigContract {

    @ConfigFlags({"n", "name"})
    String name();

    @ConfigFlags("desc")
    String description();

    @ConfigFlags("s")
    Integer size();

    @ConfigFlags("c")
    int count();

    @ConfigFlags("correct")
    boolean isCorrect();

    @ConfigFlags("done")
    Boolean isDone();

}
