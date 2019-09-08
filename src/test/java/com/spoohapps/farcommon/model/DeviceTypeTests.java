package com.spoohapps.farcommon.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private Stream<Arguments> deviceTypeCombinations() {
        return Stream.of(
                Arguments.of(DeviceType.door_lock, "4f"),
                Arguments.of(DeviceType.window_shade, "6b"));
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

    @Test
    public void shouldSerializeToJson() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        assertEquals("\"door_lock\"", mapper.writeValueAsString(DeviceType.door_lock));

    }

}
