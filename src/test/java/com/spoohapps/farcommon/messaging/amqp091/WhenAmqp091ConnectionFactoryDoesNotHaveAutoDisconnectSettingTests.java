package com.spoohapps.farcommon.messaging.amqp091;

import com.spoohapps.farcommon.messaging.PublisherConnection;
import com.spoohapps.farcommon.testhelpers.FakeNoopAmqp091Channel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WhenAmqp091ConnectionFactoryDoesNotHaveAutoDisconnectSettingTests {

    private Amqp091ConnectionSupplier mockConnectionSupplier;
    private Amqp091Connection mockConnection;

    private Amqp091ConnectionFactory factory;

    private String expectedExchange = "some.exchange";
    private String expectedQueue = "expectedQueue";
    private String expectedRoutingKey = "some.topic";

    @Captor
    private ArgumentCaptor<Consumer<String>> closeCaptor;

    @BeforeAll
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setup() throws IOException, TimeoutException {
        mockConnectionSupplier = mock(Amqp091ConnectionSupplier.class);
        mockConnection = mock(Amqp091Connection.class);
        when(mockConnectionSupplier.newConnection()).thenReturn(mockConnection);
        when(mockConnection.createChannel()).thenAnswer(i -> new FakeNoopAmqp091Channel());
        factory = new Amqp091ConnectionFactory(mockConnectionSupplier, false);
    }

    @Test
    public void shouldNotCloseUnderlyingConnectionWhenAllChannelsClosed() throws IOException, TimeoutException {
        PublisherConnection publisher = factory.newPublisherConnection(
                new Amqp091PublisherConnectionSettings(
                        expectedExchange
                ));
        publisher.open();
        publisher.close();
        verify(mockConnection, times(0)).close();
    }
}
