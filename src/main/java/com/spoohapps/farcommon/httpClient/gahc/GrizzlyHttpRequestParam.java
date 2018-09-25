package com.spoohapps.farcommon.httpClient.gahc;

import com.ning.http.client.Param;
import com.spoohapps.farcommon.httpClient.HttpRequestParam;

public class GrizzlyHttpRequestParam implements HttpRequestParam {

    private final Param param;

    public GrizzlyHttpRequestParam(Param param) {
        this.param = param;
    }

    @Override
    public String getName() {
        return param.getName();
    }

    @Override
    public String getValue() {
        return param.getValue();
    }
}
