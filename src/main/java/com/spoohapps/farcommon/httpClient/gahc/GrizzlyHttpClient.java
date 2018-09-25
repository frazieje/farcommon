package com.spoohapps.farcommon.httpClient.gahc;

import com.ning.http.client.AsyncHttpClient;

import com.spoohapps.farcommon.httpClient.HttpClient;
import com.spoohapps.farcommon.httpClient.HttpRequestBuilder;

public class GrizzlyHttpClient implements HttpClient {

    private final AsyncHttpClient client;

    public GrizzlyHttpClient(AsyncHttpClient client) {
        this.client = client;
    }


    @Override
    public HttpRequestBuilder prepareGet(String url) {
        return new GrizzlyHttpRequestBuilder(client.prepareGet(url));
    }

    @Override
    public HttpRequestBuilder preparePost(String url) {
        return new GrizzlyHttpRequestBuilder(client.preparePost(url));
    }

    @Override
    public HttpRequestBuilder preparePut(String url) {
        return new GrizzlyHttpRequestBuilder(client.preparePut(url));
    }

    @Override
    public HttpRequestBuilder prepareDelete(String url) {
        return new GrizzlyHttpRequestBuilder(client.prepareDelete(url));
    }


}
