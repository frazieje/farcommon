package com.spoohapps.farcommon.httpClient.gahc;

import com.ning.http.client.Response;

import com.spoohapps.farcommon.httpClient.HttpCookie;
import com.spoohapps.farcommon.httpClient.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

public class GrizzlyHttpResponse implements HttpResponse {

    private final Response response;

    public GrizzlyHttpResponse(Response response) {
        this.response = response;
    }


    @Override
    public int getStatusCode() {
        return response.getStatusCode();
    }

    @Override
    public String getStatusText() {
        return response.getStatusText();
    }

    @Override
    public byte[] getResponseBodyAsBytes() throws IOException {
        return response.getResponseBodyAsBytes();
    }

    @Override
    public ByteBuffer getResponseBodyAsByteBuffer() throws IOException {
        return response.getResponseBodyAsByteBuffer();
    }

    @Override
    public InputStream getResponseBodyAsStream() throws IOException {
        return response.getResponseBodyAsStream();
    }

    @Override
    public String getResponseBodyExcerpt(int maxLength, String charset) throws IOException {
        return response.getResponseBodyExcerpt(maxLength, charset);
    }

    @Override
    public String getResponseBody(String charset) throws IOException {
        return response.getResponseBody(charset);
    }

    @Override
    public String getResponseBodyExcerpt(int maxLength) throws IOException {
        return response.getResponseBodyExcerpt(maxLength);
    }

    @Override
    public String getResponseBody() throws IOException {
        return response.getResponseBody();
    }

    @Override
    public String getContentType() {
        return response.getContentType();
    }

    @Override
    public String getHeader(String name) {
        return response.getHeader(name);
    }

    @Override
    public List<String> getHeaders(String name) {
        return response.getHeaders(name);
    }

    @Override
    public boolean isRedirected() {
        return response.isRedirected();
    }

    @Override
    public List<HttpCookie> getCookies() {
        return response.getCookies().stream()
                .map(GrizzlyHttpCookie::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasResponseStatus() {
        return response.hasResponseStatus();
    }

    @Override
    public boolean hasResponseHeaders() {
        return response.hasResponseHeaders();
    }

    @Override
    public boolean hasResponseBody() {
        return response.hasResponseBody();
    }
}
