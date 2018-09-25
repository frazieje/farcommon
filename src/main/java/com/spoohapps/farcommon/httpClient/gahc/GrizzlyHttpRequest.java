package com.spoohapps.farcommon.httpClient.gahc;

import com.ning.http.client.Request;

import com.spoohapps.farcommon.httpClient.HttpRequest;

public class GrizzlyHttpRequest implements HttpRequest {

    private final Request grizzlyRequest;

    public GrizzlyHttpRequest(Request grizzlyRequest) {
        this.grizzlyRequest = grizzlyRequest;
    }



}
