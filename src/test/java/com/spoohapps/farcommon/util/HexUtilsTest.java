package com.spoohapps.farcommon.util;

import org.junit.Test;
import org.junit.jupiter.api.TestInstance;

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

}
