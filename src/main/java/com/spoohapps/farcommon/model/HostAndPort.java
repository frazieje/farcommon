package com.spoohapps.farcommon.model;

import java.net.URI;
import java.util.Objects;

public class HostAndPort {

    private final String host;

    private final int port;

    public HostAndPort(String hostAndPort) {
        this(URI.create("any://" + hostAndPort));
    }

    private HostAndPort(URI uri) {
        this(uri.getHost(), uri.getPort());
    }

    private HostAndPort(String host, int port) {
        this.host = host;
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("port " + port + " is out of range [0 - 65535]");
        }
        this.port = port;
    }

    public String getHost () {
        return host;
    }

    public int getPort () {
        return port;
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }

    @Override
    public boolean equals(Object other) {

        if (other == null) {
            return false;
        }

        HostAndPort otherHostAndPort = (HostAndPort) other;

        if (host == null) {
            if (otherHostAndPort.host != null) {
                return false;
            }
        } else {
            if (otherHostAndPort.host == null) {
                return false;
            }
            if (!host.equals(otherHostAndPort.host)) {
                return false;
            }
        }

        return port == otherHostAndPort.port;

    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}