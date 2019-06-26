package com.spoohapps.farcommon.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EUI48AddressTests {

    @Test
    public void shouldConstructFromValidAddressString() {

        String addressString = "78:4f:43:62:6f:58";

        EUI48Address address = new EUI48Address(addressString);

        assertEquals(address.toString().toLowerCase(), addressString);

    }

    @Test
    public void shouldConstructFromHyphenatedValidAddressString() {

        String addressString = "78-4f-43-62-6f-58";

        EUI48Address address = new EUI48Address(addressString);

        assertNotNull(address);
    }

    @Test
    public void shouldConstructFromCollapsedAddressString() {

        String addressString = "784f43626f58";

        EUI48Address address = new EUI48Address(addressString);

        assertEquals("78:4F:43:62:6F:58", address.toString());
    }

    @Test
    public void shouldNotConstructFromInvalidAddressString() {

        String addressString = "78:4f:43:62:6fz58";

        assertThrows(IllegalArgumentException.class, () -> {
            EUI48Address address = new EUI48Address(addressString);
        });

    }

    @Test
    public void shouldConstructFromBytes() {

        String addressString = "78:4f:43:62:6f:58";

        EUI48Address address = new EUI48Address(new byte[] { 120,79,67,98,111,88 });

        assertEquals(address.toString().toLowerCase(), addressString);

    }

    @Test
    public void shouldConstructWithName() {

        String addressString = "78:4f:43:62:6f:58";

        String expectedName = "testName";
        EUI48Address address = new EUI48Address(new byte[] { 120,79,67,98,111,88 }, expectedName);

        assertTrue(
                address.toString().toLowerCase().equals(addressString + " " + expectedName.toLowerCase())
                        && address.getName().equals(expectedName)
        );

    }

    @Test
    public void shouldConstructWithNameInString() {

        String expectedName = "testName";
        String addressString = "78:4f:43:62:6f:58";

        EUI48Address address = new EUI48Address(addressString + " " + expectedName);

        assertTrue(
                address.toString().toLowerCase().equals(addressString + " " + expectedName.toLowerCase())
                        && address.getName().equals(expectedName)
        );

    }

}
