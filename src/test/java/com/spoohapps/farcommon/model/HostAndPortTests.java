package com.spoohapps.farcommon.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HostAndPortTests {

    private static final String goodHost = "some.host.com";
    private static final int goodPort = 1234;
    private static final String goodHostAndPort = goodHost + ":" + goodPort;
    private static final String badHost = "some.b#@ !-_%3s.com";
    private static final int badPort = 185090;

    @Test
    public void shouldParseHostCorrectly() {
        HostAndPort hap = new HostAndPort(goodHostAndPort);
        assertEquals(goodHost, hap.getHost());
    }

    @Test
    public void shouldParsePortCorrectly() {
        HostAndPort hap = new HostAndPort(goodHostAndPort);
        assertEquals(goodPort, hap.getPort());
    }

    @Test
    public void shouldThrowIfBadHost() {
        assertThrows(IllegalArgumentException.class, () -> new HostAndPort(badHost + ":" + goodPort));
    }

    @Test
    public void shouldThrowIfBadPort() {
        assertThrows(IllegalArgumentException.class, () -> new HostAndPort(goodHost + ":" + badPort));
    }

    @Test
    public void shouldThrowIfBad() {
        assertThrows(IllegalArgumentException.class, () -> new HostAndPort("askldjf**()&U()23i4trjk"));
    }

    @Test
    public void shouldCorrectlyEvaluateEquals() {
        HostAndPort first = new HostAndPort(goodHostAndPort);
        HostAndPort second = new HostAndPort(goodHostAndPort);
        assertEquals(first, second);
    }

    @Test
    public void shouldCorrectlyEvaluateNotEquals() {
        HostAndPort first = new HostAndPort(goodHostAndPort);
        HostAndPort second = new HostAndPort("some.other.com:1234");
        assertNotEquals(first, second);
    }

    @Test
    public void shouldCorrectlyEvaluateNotEqualsWithDifferentPort() {
        HostAndPort first = new HostAndPort(goodHostAndPort);
        HostAndPort second = new HostAndPort(goodHost + ":1235");
        assertNotEquals(first, second);
    }

    @Test
    public void shouldCorrectlyEvaluateNotEqualsWithDifferentHost() {
        HostAndPort first = new HostAndPort(goodHostAndPort);
        HostAndPort second = new HostAndPort("some.other.com:" + goodPort);
        assertNotEquals(first, second);
    }

    @Test
    public void shouldCorrectlyEvaluateHashCodeEquals() {
        HostAndPort first = new HostAndPort(goodHostAndPort);
        HostAndPort second = new HostAndPort(goodHostAndPort);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void shouldCorrectlyEvaluateHashCodeNotEquals() {
        HostAndPort first = new HostAndPort(goodHostAndPort);
        HostAndPort second = new HostAndPort("some.other.com:1235");
        assertNotEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void shouldConvertToString() {
        assertEquals(goodHostAndPort, new HostAndPort(goodHostAndPort).toString());
    }

}
