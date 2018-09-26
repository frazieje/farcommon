package com.spoohapps.farcommon.httpClient;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Collection;
import java.util.List;

public interface HttpRequest {

    String getMethod();

    String getUrl();

    InetAddress getInetAddress();

    InetAddress getLocalAddress();

    Collection<HttpCookie> getCookies();

    byte[] getByteData();

    List<byte[]> getCompositeByteData();

    String getStringData();

    InputStream getStreamData();

    long getContentLength();

    List<HttpRequestParam> getFormParams();

    String getVirtualHost();

    List<HttpRequestParam> getQueryParams();

    File getFile();

    Boolean getFollowRedirect();

    int getRequestTimeout();

    long getRangeOffset();

    String getBodyEncoding();

}
