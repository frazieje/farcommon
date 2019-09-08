package com.spoohapps.farcommon.model;

import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceTypeTests {

    @Test
    public void shouldResolveShortHexDeviceType() {
        assertEquals("4f", DeviceType.door_lock.asString());
    }

    @ParameterizedTest
    @MethodSource("deviceTypeCombinations")
    public void shouldResolveDeviceTypeFromString(DeviceType expectedDeviceType, String inputHexString) {
        assertEquals(expectedDeviceType, DeviceType.fromString(inputHexString));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForUnknownDeviceType() {
        assertEquals(DeviceType.door_lock, DeviceType.fromString("ffff"));
    }


    private Stream<Arguments> deviceTypeCombinations() {
        return Stream.of(
                Arguments.of(DeviceType.door_lock, "4f"),
                Arguments.of(DeviceType.window_shade, "6b"));
    }
}
