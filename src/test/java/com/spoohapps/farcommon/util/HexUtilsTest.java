package com.spoohapps.farcommon.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HexUtilsTest {

    @Test
    public void shouldConvertByteArrayToHexStringAndDefaultToTrimLeadingZeroes() {
        assertEquals("4f", HexUtils.intToHex(79));
    }

    @Test
    public void shouldConvertByteArrayToHexStringAndNotTrimLeadingZeroes() {
        assertEquals("0000004f", HexUtils.intToHex(79, true));
    }

    @ParameterizedTest
    @ValueSource(strings = { "0000004f", "000004f", "00004f", "0004f", "004f", "04f", "4f" })
    public void shouldConvertZeroPaddedStringToInt(String hexValue) {
        assertEquals(79, HexUtils.hexToInt("0000004f"));
    }

    @ParameterizedTest
    @MethodSource("hexStringToIntArguments")
    public void shouldConvertStringToInt(Integer integer, String stringValue) {
        assertEquals(integer.intValue(), HexUtils.hexToInt(stringValue));
    }

    private Stream<Arguments> hexStringToIntArguments() {
        return Stream.of(
                Arguments.of(15, "f"),
                Arguments.of(79, "4f"),
                Arguments.of(65535, "ffff"),
                Arguments.of(3535, "dcf"),
                Arguments.of(255, "ff"),
                Arguments.of(134, "86"),
                Arguments.of(107, "6b"));
    }
}
