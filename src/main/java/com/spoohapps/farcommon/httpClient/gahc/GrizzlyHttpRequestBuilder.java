package com.spoohapps.farcommon.httpClient.gahc;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Param;
import com.ning.http.client.cookie.Cookie;
import com.spoohapps.farcommon.httpClient.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class GrizzlyHttpRequestBuilder implements HttpRequestBuilder {

    public AsyncHttpClient.BoundRequestBuilder requestBuilder;

    public GrizzlyHttpRequestBuilder(AsyncHttpClient.BoundRequestBuilder requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    @Override
    public HttpRequestBuilder addCookie(HttpCookie cookie) {
        requestBuilder.addCookie(
                new Cookie(
                    cookie.getName(),
                    cookie.getValue(),
                    cookie.isWrap(),
                    cookie.getDomain(),
                    cookie.getPath(),
                    cookie.getMaxAge(),
                    cookie.isSecure(),
                    cookie.isHttpOnly()));
        return this;
    }

    @Override
    public HttpRequestBuilder addHeader(String name, String value) {
        requestBuilder.addHeader(name, value);
        return this;
    }

    @Override
    public HttpRequestBuilder addFormParam(String key, String value) {
        requestBuilder.addFormParam(key, value);
        return this;
    }

    @Override
    public HttpRequestBuilder addQueryParam(String name, String value) {
        requestBuilder.addQueryParam(name, value);
        return this;
    }

    @Override
    public HttpRequestBuilder addQueryParams(List<HttpRequestParam> queryParams) {
        requestBuilder.addQueryParams(
                queryParams.stream()
                        .map(p -> new Param(p.getName(), p.getValue()))
                        .collect(Collectors.toList()));
        return this;
    }

    @Override
    public HttpRequestBuilder setQueryParams(List<HttpRequestParam> params) {
        requestBuilder.setQueryParams(
                params.stream()
                        .map(p -> new Param(p.getName(), p.getValue()))
                        .collect(Collectors.toList()));
        return this;
    }

    @Override
    public HttpRequestBuilder setQueryParams(Map<String, List<String>> params) {
        requestBuilder.setQueryParams(params);
        return this;
    }

    @Override
    public HttpRequest build() {
        return new GrizzlyHttpRequest(requestBuilder.build());
    }

    @Override
    public HttpRequestBuilder setBody(byte[] data) {
        requestBuilder.setBody(data);
        return this;
    }

    @Override
    public HttpRequestBuilder setBody(String data) {
        requestBuilder.setBody(data);
        return this;
    }

    @Override
    public HttpRequestBuilder setHeader(String name, String value) {
        requestBuilder.setHeader(name, value);
        return this;
    }

    @Override
    public HttpRequestBuilder setHeaders(Map<String, Collection<String>> headers) {
        requestBuilder.setHeaders(headers);
        return this;
    }

    @Override
    public HttpRequestBuilder setFormParams(List<HttpRequestParam> params) {
        requestBuilder.setFormParams(
                params.stream()
                        .map(p -> new Param(p.getName(), p.getValue()))
                        .collect(Collectors.toList()));
        return this;
    }

    @Override
    public HttpRequestBuilder setFormParams(Map<String, List<String>> params) {
        requestBuilder.setFormParams(params);
        return this;
    }

    @Override
    public HttpRequestBuilder setMethod(String method) {
        requestBuilder.setMethod(method);
        return this;
    }

    @Override
    public HttpRequestBuilder setUrl(String url) {
        requestBuilder.setUrl(url);
        return this;
    }

    @Override
    public HttpRequestBuilder setVirtualHost(String virtualHost) {
        requestBuilder.setVirtualHost(virtualHost);
        return this;
    }

    @Override
    public HttpRequestBuilder setFollowRedirects(boolean followRedirects) {
        requestBuilder.setFollowRedirects(followRedirects);
        return this;
    }

    @Override
    public HttpRequestBuilder addOrReplaceCookie(HttpCookie c) {
        requestBuilder.addOrReplaceCookie(
                new Cookie(
                        c.getName(),
                        c.getValue(),
                        c.isWrap(),
                        c.getDomain(),
                        c.getPath(),
                        c.getMaxAge(),
                        c.isSecure(),
                        c.isHttpOnly()));
        return this;
    }

    @Override
    public Future<HttpResponse> execute() {
        return new Future<HttpResponse>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return requestBuilder.execute().cancel(mayInterruptIfRunning);
            }

            @Override
            public boolean isCancelled() {
                return requestBuilder.execute().isCancelled();
            }

            @Override
            public boolean isDone() {
                return requestBuilder.execute().isDone();
            }

            @Override
            public HttpResponse get() throws InterruptedException, ExecutionException {
                return new GrizzlyHttpResponse(requestBuilder.execute().get());
            }

            @Override
            public HttpResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return new GrizzlyHttpResponse(requestBuilder.execute().get(timeout, unit));
            }
        };
    }
}
