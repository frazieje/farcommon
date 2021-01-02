package com.spoohapps.farcommon.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MACAddressTests {

    private final ObjectMapper mapper;

    public MACAddressTests() {
        mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    public void shouldConstructEUI48FromValidAddressString() {

        String addressString = "78:4f:43:62:6f:58";

        MACAddress address = new MACAddress(addressString);

        assertEquals(address.toString().toLowerCase(), addressString);

    }

    @Test
    public void shouldConstructEUI48FromHyphenatedValidAddressString() {

        String addressString = "78-4f-43-62-6f-58";

        MACAddress address = new MACAddress(addressString);

        assertNotNull(address);
    }

    @Test
    public void shouldConstructEUI48FromCollapsedAddressString() {

        String addressString = "784f43626f58";

        MACAddress address = new MACAddress(addressString);

        assertEquals("78:4F:43:62:6F:58", address.toString());
    }

    @Test
    public void shouldNotConstructEUI48FromInvalidAddressString() {

        String addressString = "78:4f:43:62:6fz58";

        assertThrows(IllegalArgumentException.class, () -> {
            MACAddress address = new MACAddress(addressString);
        });

    }

    @Test
    public void shouldConstructEUI48FromBytes() {

        String addressString = "78:4f:43:62:6f:58";

        MACAddress address = new MACAddress(new byte[] { 120,79,67,98,111,88 });

        assertEquals(address.toString().toLowerCase(), addressString);

    }

    @Test
    public void shouldConstructEUI48WithName() {

        String addressString = "78:4f:43:62:6f:58";

        String expectedName = "testName";
        MACAddress address = new MACAddress(new byte[] { 120,79,67,98,111,88 }, expectedName);

        assertTrue(
                address.toString().toLowerCase().equals(addressString + " " + expectedName.toLowerCase())
                        && address.getName().equals(expectedName)
        );

    }

    @Test
    public void shouldConstructEUI48WithNameInString() {

        String expectedName = "testName";
        String addressString = "78:4f:43:62:6f:58";

        MACAddress address = new MACAddress(addressString + " " + expectedName);

        assertTrue(
                address.toString().toLowerCase().equals(addressString + " " + expectedName.toLowerCase())
                        && address.getName().equals(expectedName)
        );

    }

    @Test
    public void shouldConstructEUI64FromValidAddressString() {

        String addressString = "78:4f:43:62:6f:58:5c:9a";

        MACAddress address = new MACAddress(addressString);

        assertEquals(address.toString().toLowerCase(), addressString);

    }

    @Test
    public void shouldConstructEUI64FromHyphenatedValidAddressString() {

        String addressString = "78-4f-43-62-6f-58-5c-9a";

        MACAddress address = new MACAddress(addressString);

        assertNotNull(address);
    }

    @Test
    public void shouldConstructEUI64FromCollapsedAddressString() {

        String addressString = "784f43626f58fc9a";

        MACAddress address = new MACAddress(addressString);

        assertEquals("78:4f:43:62:6f:58:fc:9a".toUpperCase(), address.toString());
    }

    @Test
    public void shouldNotConstructEUI64FromInvalidAddressString() {

        String addressString = "78:4f:43:62:6fz58";

        assertThrows(IllegalArgumentException.class, () -> {
            MACAddress address = new MACAddress(addressString);
        });

    }

    @Test
    public void shouldNotConstructEUI64FromInvalidAddressString2() {

        String addressString = "78:4f:43:62:6f:z5:80:34";

        assertThrows(IllegalArgumentException.class, () -> {
            MACAddress address = new MACAddress(addressString);
        });

    }

    @Test
    public void shouldConstructEUI64FromBytes() {

        String addressString = "78:4f:43:62:6f:58:43:62";

        MACAddress address = new MACAddress(new byte[] { 120,79,67,98,111,88,67,98 });

        assertEquals(address.toString().toLowerCase(), addressString);

    }

    @Test
    public void shouldConstructEUI64WithName() {

        String addressString = "78:4f:43:62:6f:58:43:62";

        String expectedName = "testName";
        MACAddress address = new MACAddress(new byte[] { 120,79,67,98,111,88,67,98 }, expectedName);

        assertTrue(
                address.toString().toLowerCase().equals(addressString + " " + expectedName.toLowerCase())
                        && address.getName().equals(expectedName)
        );

    }

    @Test
    public void shouldConstructEUI64WithNameInString() {

        String expectedName = "testName";
        String addressString = "78:4f:43:62:6f:58:43:62";

        MACAddress address = new MACAddress(addressString + " " + expectedName);

        assertTrue(
                address.toString().toLowerCase().equals(addressString + " " + expectedName.toLowerCase())
                        && address.getName().equals(expectedName)
        );

    }

    @Test
    public void shouldSerializeEUI48ToJson() throws IOException {

        String expectedName = "testName";
        String addressString = "78:4f:43:62:6f:58";

        MACAddress address = new MACAddress(addressString + " " + expectedName);

        String json = mapper.writeValueAsString(address);

        assertNotNull(json);

        MACAddress hydrated = mapper.readValue(json, MACAddress.class);

        assertEquals(address, hydrated);

    }

    @Test
    public void shouldSerializeEUI64ToJson() throws IOException {

        String expectedName = "testName2";
        String addressString = "78:4f:43:62:6f:58:43:62";

        MACAddress address = new MACAddress(addressString + " " + expectedName);

        String json = mapper.writeValueAsString(address);

        assertNotNull(json);

        MACAddress hydrated = mapper.readValue(json, MACAddress.class);

        assertEquals(address, hydrated);

    }

    @Test
    public void shouldGenerateRandomEUI48LengthAddress() {
        MACAddress address = MACAddress.randomEUI48();
        assertEquals(6, address.getData().length);
    }

    @Test
    public void shouldGenerateRandomEUI64LengthAddress() {
        MACAddress address = MACAddress.randomEUI64();
        assertEquals(8, address.getData().length);
    }

}
