package com.spoohapps.farcommon.httpClient.gahc;

import com.ning.http.client.cookie.Cookie;

import com.spoohapps.farcommon.httpClient.HttpCookie;

public class GrizzlyHttpCookie implements HttpCookie {

    private final Cookie cookie;

    public GrizzlyHttpCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    @Override
    public String getDomain() {
        return cookie.getDomain();
    }

    @Override
    public String getName() {
        return cookie.getName();
    }

    @Override
    public String getValue() {
        return cookie.getValue();
    }

    @Override
    public boolean isWrap() {
        return cookie.isWrap();
    }

    @Override
    public String getPath() {
        return cookie.getPath();
    }

    @Override
    public long getMaxAge() {
        return cookie.getMaxAge();
    }

    @Override
    public boolean isSecure() {
        return cookie.isSecure();
    }

    @Override
    public boolean isHttpOnly() {
        return cookie.isHttpOnly();
    }

}
