package com.spoohapps.farcommon.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WhenGettingMessageHeadersTests {

    @Test
    public void shouldNotGetNull() {
        Message message = new Message("", new byte[] {0,1}, null);
        assertNotNull(message.getHeaders());
    }

    @Test
    public void shouldNotGetNullAfterSetter() {
        Message message = new Message("", new byte[] {0,1});
        message.setHeaders(null);
        assertNotNull(message.getHeaders());
    }

}
