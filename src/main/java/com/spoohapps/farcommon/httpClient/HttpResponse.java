package com.spoohapps.farcommon.httpClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public interface HttpResponse {

    int getStatusCode();

    String getStatusText();

    byte[] getResponseBodyAsBytes() throws IOException;

    ByteBuffer getResponseBodyAsByteBuffer() throws IOException;

    InputStream getResponseBodyAsStream() throws IOException;

    String getResponseBodyExcerpt(int maxLength, String charset) throws IOException;

    String getResponseBody(String charset) throws IOException;

    String getResponseBodyExcerpt(int maxLength) throws IOException;

    String getResponseBody() throws IOException;

    String getContentType();

    String getHeader(String name);

    List<String> getHeaders(String name);

    boolean isRedirected();

    String toString();

    List<HttpCookie> getCookies();

    boolean hasResponseStatus();

    boolean hasResponseHeaders();

    boolean hasResponseBody();

}
