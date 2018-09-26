package com.spoohapps.farcommon.httpClient.gahc;

import com.ning.http.client.Request;

import com.spoohapps.farcommon.httpClient.HttpCookie;
import com.spoohapps.farcommon.httpClient.HttpRequest;
import com.spoohapps.farcommon.httpClient.HttpRequestParam;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GrizzlyHttpRequest implements HttpRequest {

    private final Request grizzlyRequest;

    public GrizzlyHttpRequest(Request grizzlyRequest) {
        this.grizzlyRequest = grizzlyRequest;
    }


    @Override
    public String getMethod() {
        return grizzlyRequest.getMethod();
    }

    @Override
    public String getUrl() {
        return grizzlyRequest.getUrl();
    }

    @Override
    public InetAddress getInetAddress() {
        return grizzlyRequest.getInetAddress();
    }

    @Override
    public InetAddress getLocalAddress() {
        return grizzlyRequest.getLocalAddress();
    }

    @Override
    public Collection<HttpCookie> getCookies() {
        return grizzlyRequest.getCookies().stream()
                .map(GrizzlyHttpCookie::new)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] getByteData() {
        return grizzlyRequest.getByteData();
    }

    @Override
    public List<byte[]> getCompositeByteData() {
        return grizzlyRequest.getCompositeByteData();
    }

    @Override
    public String getStringData() {
        return grizzlyRequest.getStringData();
    }

    @Override
    public InputStream getStreamData() {
        return grizzlyRequest.getStreamData();
    }

    @Override
    public long getContentLength() {
        return grizzlyRequest.getContentLength();
    }

    @Override
    public List<HttpRequestParam> getFormParams() {
        return grizzlyRequest.getFormParams().stream()
                .map(GrizzlyHttpRequestParam::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getVirtualHost() {
        return grizzlyRequest.getVirtualHost();
    }

    @Override
    public List<HttpRequestParam> getQueryParams() {
        return grizzlyRequest.getQueryParams().stream()
                .map(GrizzlyHttpRequestParam::new)
                .collect(Collectors.toList());
    }

    @Override
    public File getFile() {
        return grizzlyRequest.getFile();
    }

    @Override
    public Boolean getFollowRedirect() {
        return grizzlyRequest.getFollowRedirect();
    }

    @Override
    public int getRequestTimeout() {
        return grizzlyRequest.getRequestTimeout();
    }

    @Override
    public long getRangeOffset() {
        return grizzlyRequest.getRangeOffset();
    }

    @Override
    public String getBodyEncoding() {
        return grizzlyRequest.getBodyEncoding();
    }
}
