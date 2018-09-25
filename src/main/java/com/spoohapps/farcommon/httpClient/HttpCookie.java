package com.spoohapps.farcommon.httpClient;

public interface HttpCookie {

    String getDomain();

    String getName();

    String getValue();

    boolean isWrap();

    String getPath();

    long getMaxAge();

    boolean isSecure();

    boolean isHttpOnly();

}
