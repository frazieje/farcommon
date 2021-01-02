package com.spoohapps.farcommon.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class DeviceIdTests {

    private static Stream<Arguments> deviceIdCombinations() {
        return Stream.of(
                Arguments.of(
                        new DeviceId(DeviceType.window_shade, new MACAddress("78:4f:43:62:6f:58:43:62")),
                        new DeviceId(DeviceType.window_shade, new MACAddress(new byte[] { 120,79,67,98,111,88,67,98 })),
                        true),
                Arguments.of(
                        new DeviceId(DeviceType.door_lock, new MACAddress("78:4f:43:62:6f:58:43:62")),
                        new DeviceId(DeviceType.window_shade, new MACAddress(new byte[] { 120,79,67,98,111,88,67,98 })),
                        false),
                Arguments.of(
                        new DeviceId(DeviceType.window_shade, new MACAddress("78:4f:43:62:6f:58:43:62")),
                        new DeviceId(DeviceType.fromString("006b"), new MACAddress(new byte[] { 120,79,67,98,111,88,67,98 })),
                        true),
                Arguments.of(
                        new DeviceId(DeviceType.window_shade, new MACAddress("78:4f:43:62:6f:58")),
                        new DeviceId(DeviceType.fromString("006b"), new MACAddress(new byte[] { 120,79,67,98,111,88 })),
                        true),
                Arguments.of(
                        new DeviceId(DeviceType.window_shade, new MACAddress("78:4f:43:62:6f:58")),
                        new DeviceId(DeviceType.fromString("004f"), new MACAddress(new byte[] { 120,79,67,98,111,88 })),
                        false),
                Arguments.of(
                        new DeviceId(DeviceType.door_lock, new MACAddress("78:4f:43:62:6f:58:43:61")),
                        new DeviceId(DeviceType.door_lock, new MACAddress(new byte[] { 120,79,67,98,111,88,67,98 })),
                        false)
        );
    }
    @ParameterizedTest
    @MethodSource("deviceIdCombinations")
    public void shouldEvaluateEquals(DeviceId id, DeviceId id2, boolean test) {
        boolean eval = id.equals(id2);
        assertEquals(test, eval);
    }

    private static Stream<Arguments> deviceIdFromStringCombinations() {
        return Stream.of(
                Arguments.of(
                        "6bb16c43a66f51a493",
                        new DeviceId(DeviceType.window_shade, new MACAddress("b1:6c:43:a6:6f:51:a4:93")),
                        true),
                Arguments.of(
                        "4f784f43626f584362",
                        new DeviceId(DeviceType.door_lock, new MACAddress("78:4f:43:62:6f:58:43:62")),
                        true),
                Arguments.of(
                        "6b784f43626f584362",
                        new DeviceId(DeviceType.window_shade, new MACAddress("78:4f:43:62:6f:58:43:62")),
                        true),
                Arguments.of(
                        "6bb16c43a66f51",
                        new DeviceId(DeviceType.window_shade, new MACAddress("b1:6c:43:a6:6f:51")),
                        true),
                Arguments.of(
                        "6b784f43626f58",
                        new DeviceId(DeviceType.window_shade, new MACAddress("78:4f:43:62:6f:58")),
                        true),
                Arguments.of(
                        "6b784f43626f59",
                        new DeviceId(DeviceType.window_shade, new MACAddress("78:4f:43:62:6f:58")),
                        false)
        );
    }
    @ParameterizedTest
    @MethodSource("deviceIdFromStringCombinations")
    public void shouldConvertFromString(String deviceId, DeviceId id2, boolean test) {
        boolean eval = new DeviceId(deviceId).equals(id2);
        assertEquals(test, eval);
    }

}
