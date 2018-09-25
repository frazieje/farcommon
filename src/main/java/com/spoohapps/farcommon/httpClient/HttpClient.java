package com.spoohapps.farcommon.httpClient;

public interface HttpClient {

    HttpRequestBuilder prepareGet(String url);

    HttpRequestBuilder preparePost(String url);

    HttpRequestBuilder preparePut(String url);

    HttpRequestBuilder prepareDelete(String url);

}
