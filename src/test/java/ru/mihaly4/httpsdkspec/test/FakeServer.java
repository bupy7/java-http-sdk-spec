package ru.mihaly4.httpsdkspec.test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class FakeServer extends Dispatcher {
    private static final String RESPONSE_CONTENT_TYPE = "Content-Type: application/json; charset=utf-8";

    private final int port;
    private int responseCode = 200;
    private MockWebServer server;

    public FakeServer(int port) {
        this.port = port;
    }

    @Override
    public MockResponse dispatch(RecordedRequest request) {
        String path = request.getRequestUrl().url().getPath();
        try {
            if (!path.startsWith("/") || path.contains("..")) {
                throw new FileNotFoundException();
            }

            String fixture = path.toLowerCase();
            if (path.replaceFirst("^/", "").split("/").length == 1) {
                fixture = fixture.concat("/index");
            }
            fixture = path.replace("-", "");

            URL response = getClass().getResource("/fixture/response"
                        + fixture
                        + String.valueOf(responseCode)
                        + ".json");
            if (response == null) {
                throw new NullPointerException(String.format("Not found response for \"%s\"", path));
            }

            return new MockResponse()
                    .setResponseCode(responseCode)
                    .setBody(Resources.toString(response, Charsets.UTF_8))
                    .addHeader(RESPONSE_CONTENT_TYPE);

        } catch (FileNotFoundException | NullPointerException e) {
            return new MockResponse()
                    .setResponseCode(404)
                    .addHeader(RESPONSE_CONTENT_TYPE)
                    .setBody("[]");
        } catch (IOException e) {
            return new MockResponse()
                    .setResponseCode(500)
                    .addHeader(RESPONSE_CONTENT_TYPE)
                    .setBody("[]");
        }
    }

    public void run() throws IOException {
        server = new MockWebServer();
        server.setDispatcher(this);
        server.start(port);
    }

    public void stop() throws IOException {
        server.close();
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
