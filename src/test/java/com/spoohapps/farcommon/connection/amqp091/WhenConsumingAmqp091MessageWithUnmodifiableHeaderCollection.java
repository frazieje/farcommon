package com.spoohapps.farcommon.connection.amqp091;

import com.spoohapps.farcommon.model.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WhenConsumingAmqp091MessageWithUnmodifiableHeaderCollection {
    private Amqp091ConsumerConnection connection;
    private Amqp091ChannelSupplier mockChannelSupplier;
    private Amqp091Channel mockChannel;

    private boolean closed;

    private String expectedExchange = "some.exchange";
    private String expectedQueue = "SomeQueue";
    private String expectedRoutingKey = "some.routing.key";

    private Message consumedMessage;

    @BeforeAll
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setup() throws IOException, TimeoutException {
        mockChannelSupplier = mock(Amqp091ChannelSupplier.class);
        mockChannel = mock(Amqp091Channel.class);
        connection = new Amqp091ConsumerConnection(mockChannelSupplier, expectedExchange, expectedQueue, expectedRoutingKey);
        connection.onConsume(message -> consumedMessage = message);
        connection.onClosed(() -> closed = true);
        consumedMessage = null;
        closed = false;
        when(mockChannelSupplier.getChannel()).thenReturn(mockChannel);
        connection.open();
        verify(mockChannel).consume(any(), messageCaptor.capture(), any());
        Amqp091Message mockMessage = mock(Amqp091Message.class);
        when(mockMessage.getHeaders()).thenReturn(Collections.unmodifiableMap(new HashMap<>()));
        messageCaptor.getValue().accept(mockMessage);
    }

    @Captor
    private ArgumentCaptor<Consumer<Amqp091Message>> messageCaptor;

    @Test
    public void shouldMakeHeaderCollectionModifiable() throws IOException, TimeoutException {
        String headerKey = "someheader";
        String expected = "value";
        consumedMessage.getHeaders().put(headerKey, expected);
        assertEquals(consumedMessage.getHeader(headerKey), expected);
    }

}
