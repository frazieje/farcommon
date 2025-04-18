package com.spoohapps.farcommon.config;

import com.spoohapps.farcommon.Config;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WhenLoadingSimpleConfigTests {

    private TestConfigContract testConfig;

    private final String expectedName = "some name";
    private final String expectedDescription = "some description";
    private final int expectedInt = 12;
    private final Integer expectedInteger = 24;
    private final boolean expectedBool = true;
    private final Boolean expectedBoolean = Boolean.FALSE;
    private final List<String> expectedStringList = Arrays.asList("string1", "string2");
    private final List<Integer> expectedIntegerList = Arrays.asList(0,1,2,3);

    @BeforeAll
    public void setup() {
        testConfig =
                Config.from(TestConfigContract.class)
                        .apply(new String[]{
                                "-n", expectedName,
                                "-desc", expectedDescription,
                                "-c", String.valueOf(expectedInt),
                                "-s", expectedInteger.toString(),
                                "-correct", String.valueOf(expectedBool),
                                "-done", String.valueOf(expectedBoolean),
                                "-stringlist", String.join(",", expectedStringList),
                                "-integerlist", expectedIntegerList.stream().map(String::valueOf).collect(Collectors.joining(","))
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

    @Test
    public void shouldReturnNullForUnspecifiedBoxedType() {
        assertNull(testConfig.integerUnspecified());
    }

    @Test
    public void shouldReturnDefaultValueForPrimitiveType() {
        assertEquals(0, testConfig.primitiveUnspecified());
    }

    @Test
    public void shouldReturnListOfStrings() {
        assertIterableEquals(expectedStringList, testConfig.stringList());
    }

    @Test
    public void shouldReturnListOfIntegers() {
        assertIterableEquals(expectedIntegerList, testConfig.integerList());
    }


    private interface TestConfigContract {

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

        @ConfigFlags("nonexistent")
        Integer integerUnspecified();

        @ConfigFlags("alsononexistent")
        int primitiveUnspecified();

        @ConfigFlags("stringlist")
        List<String> stringList();

        @ConfigFlags("integerlist")
        List<Integer> integerList();

    }

}
