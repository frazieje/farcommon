package com.spoohapps.farcommon.model;

import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceTypeTests {

    @Test
    public void shouldResolveShortHexDeviceType() {
        assertEquals("4f", DeviceType.door_lock.asString());
    }

}
