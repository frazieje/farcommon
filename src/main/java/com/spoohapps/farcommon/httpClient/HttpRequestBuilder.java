package com.spoohapps.farcommon.httpClient;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface HttpRequestBuilder {

     HttpRequestBuilder addCookie(HttpCookie cookie);

     HttpRequestBuilder addHeader(String name, String value);

     HttpRequestBuilder addFormParam(String key, String value);

     HttpRequestBuilder addQueryParam(String name, String value);

     HttpRequestBuilder addQueryParams(List<HttpRequestParam> queryParams);

     HttpRequestBuilder setQueryParams(List<HttpRequestParam> params);

     HttpRequestBuilder setQueryParams(Map<String, List<String>> params);

     HttpRequest build();

     HttpRequestBuilder setBody(byte[] data);

     HttpRequestBuilder setBody(String data);

     HttpRequestBuilder setHeader(String name, String value);

     HttpRequestBuilder setHeaders(Map<String, Collection<String>> headers);

     HttpRequestBuilder setFormParams(List<HttpRequestParam> params);

     HttpRequestBuilder setFormParams(Map<String, List<String>> params);

     HttpRequestBuilder setMethod(String method);

     HttpRequestBuilder setUrl(String url);

     HttpRequestBuilder setVirtualHost(String virtualHost);

     HttpRequestBuilder setFollowRedirects(boolean followRedirects);

     HttpRequestBuilder addOrReplaceCookie(HttpCookie c);

     Future<HttpResponse> execute();

}
