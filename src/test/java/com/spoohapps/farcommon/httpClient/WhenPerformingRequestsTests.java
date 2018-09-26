package com.spoohapps.farcommon.httpClient;

import com.ning.http.client.AsyncHttpClient;
import com.spoohapps.farcommon.httpClient.gahc.GrizzlyHttpClient;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WhenPerformingRequestsTests {

    private MockWebServer mockWebServer;

    private HttpClient httpClient;

    private String testUrl;

    @BeforeAll
    private void setup() throws IOException {
        mockWebServer = new MockWebServer();

        mockWebServer.start();

        testUrl = mockWebServer.url("/test/").toString();

        httpClient = new GrizzlyHttpClient(new AsyncHttpClient());
    }

    @Test
    public void shouldReturnBody() throws ExecutionException, InterruptedException, IOException {
        String expected = "hello";
        mockWebServer.enqueue(new MockResponse().setBody(expected));

        HttpResponse response = httpClient.prepareGet(testUrl).execute().get();

        assertEquals(expected, response.getResponseBody());
    }

    @Test
    public void shouldReturnStatusCode() throws ExecutionException, InterruptedException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        HttpResponse response = httpClient.prepareGet(testUrl).execute().get();

        assertEquals(500, response.getStatusCode());
    }

}
