package com.spoohapps.farcommon.connection.amqp091;

import com.spoohapps.farcommon.connection.PublisherConnection;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WhenRecyclingAmqp091ConnectionFactoryWithoutAutoDisconnectSettingTests {
    private Amqp091ConnectionSupplier mockConnectionSupplier;
    private Amqp091Connection mockConnection;

    private Amqp091ConnectionFactory factory;

    private String expectedExchange = "some.exchange";

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
        PublisherConnection publisher = factory.newPublisherConnection(
                new Amqp091PublisherConnectionSettings(
                        expectedExchange
                ));
        publisher.open();
        factory.recycle();
    }

    @Test
    public void shouldCloseUnderlyingConnection() throws IOException, TimeoutException {
        verify(mockConnection).close();
    }
}
