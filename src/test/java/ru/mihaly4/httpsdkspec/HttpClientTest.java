package ru.mihaly4.httpsdkspec;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.Response;
import ru.mihaly4.httpsdkspec.test.Env;
import ru.mihaly4.httpsdkspec.test.FakeServer;
import ru.mihaly4.httpsdkspec.test.HttpClient;
import ru.mihaly4.httpsdkspec.test.ICredentials;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HttpClientTest {
    private Credentials credentials = new Credentials();
    private HttpClient httpClient = new HttpClient(
            credentials,
            String.format("http://localhost:%d", Env.getFakeServerPort())
    );
    private static FakeServer fakeServer = new FakeServer(Env.getFakeServerPort());

    @BeforeClass
    public static void runServer() {
        try {
            fakeServer.run();
        } catch (IOException e) {
            System.out.println(">>> FakeServer don't run");
        }
    }

    @AfterClass
    public static void stopServer() {
        try {
            fakeServer.stop();
        } catch (IOException e) {
            System.out.println(">>> FakeServer don't stop");
        }
    }

    @Test
    public void postRequest200() throws IOException {
        fakeServer.setResponseCode(200);

        FakePostRequest fakeRequest = new FakePostRequest();
        fakeRequest.getBody().setValue1("example@email.com");
        fakeRequest.getBody().setValue2("1234password");

        credentials.setAuth(false);
        Response response = httpClient.sendFormPost(fakeRequest);

        assertNotNull(response);
        assertTrue(response.isSuccessful());
        assertEquals(
                "{\"data\":{\"userId\":1,\"id\":1,\"title\":\"Exampletitle\",\"completed\":false}}",
                response.body().string().replaceAll("\\s", "")
        );
    }

    @Test
    public void postRequest400() throws IOException {
        fakeServer.setResponseCode(400);

        FakePostRequest fakeRequest = new FakePostRequest();
        fakeRequest.getBody().setValue1("example@email.com");
        fakeRequest.getBody().setValue2("1234password");

        credentials.setAuth(false);
        Response response = httpClient.sendFormPost(fakeRequest);

        assertNotNull(response);
        assertFalse(response.isSuccessful());
        assertEquals(
                "{\"message\":\"Invalidrequest\",\"meta\":[]}",
                response.body().string().replaceAll("\\s", "")
        );
    }

    @Test
    public void postUploadRequest200() throws IOException {
        fakeServer.setResponseCode(200);

        FakePostUploadRequest fakeRequest = new FakePostUploadRequest();
        fakeRequest.getBody().setValue1("name");
        fakeRequest.getBody().setValue2("1.jpg");
        fakeRequest.getFiles().setFile1(new File(getClass().getResource("/fixture/data/1.jpg").getFile()));

        credentials.setAuth(true);
        Response response = httpClient.sendMultipartPost(fakeRequest);

        assertNotNull(response);
        assertTrue(response.isSuccessful());
        assertEquals(
                "{\"data\":{\"file\":\"/assets/1.jpg\"}}",
                response.body().string().replaceAll("\\s", "")
        );
    }

    @Test
    public void postUploadRequest400() throws IOException {
        fakeServer.setResponseCode(400);

        FakePostUploadRequest fakeRequest = new FakePostUploadRequest();
        fakeRequest.getFiles().setFile1(new File(getClass().getResource("/fixture/data/1.jpg").getFile()));

        credentials.setAuth(true);
        Response response = httpClient.sendMultipartPost(fakeRequest);

        assertNotNull(response);
        assertFalse(response.isSuccessful());
        assertEquals(
                "{\"message\":\"Nameisnotspecified\",\"meta\":[]}",
                response.body().string().replaceAll("\\s", "")
        );
    }

    @Test
    public void getRequest200() throws IOException {
        fakeServer.setResponseCode(200);

        FakeGetRequest fakeRequest = new FakeGetRequest();
        fakeRequest.getParams().setValue1("some value 1");
        fakeRequest.getParams().setValue2("some value 2");

        credentials.setAuth(true);
        Response response = httpClient.sendGet(fakeRequest);

        assertNotNull(response);
        assertTrue(response.isSuccessful());
        assertEquals(
                "{\"data\":{\"id\":1,\"name\":\"LeanneGraham\"}}",
                response.body().string().replaceAll("\\s", "")
        );
    }

    @Test
    public void getRequest404() throws IOException {
        fakeServer.setResponseCode(404);

        FakeGetRequest fakeRequest = new FakeGetRequest();
        fakeRequest.getParams().setValue1("some value 1");
        fakeRequest.getParams().setValue2("some value 2");

        credentials.setAuth(true);
        Response response = httpClient.sendGet(fakeRequest);

        assertNotNull(response);
        assertFalse(response.isSuccessful());
        assertEquals(
                "{\"message\":\"Resourcenotfound\",\"meta\":[]}",
                response.body().string().replaceAll("\\s", "")
        );
    }

    private static class FakePostRequest extends AbstractFormPostRequest {
        @Nullable
        private Body body;

        @Nonnull
        @Override
        public Body getBody() {
            if (body == null) {
                body = new Body();
            }
            return body;
        }

        @Nonnull
        @Override
        public String getResource() {
            return "/fake/post";
        }

        static class Body implements IPostRequest.ISerialize<Map<String, String>> {
            @Nonnull
            private String value1 = "";
            @Nonnull
            private String value2 = "";

            @Nonnull
            @Override
            public Map<String, String> serialize() {
                Map<String, String> map = new HashMap<>();

                map.put("value1", value1);
                map.put("value2", value2);

                return map;
            }

            public void setValue1(@Nonnull String value1) {
                this.value1 = value1;
            }

            public void setValue2(@Nonnull String value2) {
                this.value2 = value2;
            }
        }
    }

    private static class FakePostUploadRequest extends AbstractMultipartPostRequest {
        @Nullable
        private Body body;
        @Nullable
        private Files files;

        @Nonnull
        @Override
        public Body getBody() {
            if (body == null) {
                body = new Body();
            }
            return body;
        }

        @Nonnull
        @Override
        public Files getFiles() {
            if (files == null) {
                files = new Files();
            }
            return files;
        }

        @Nonnull
        @Override
        public String getResource() {
            return "/fake/post-upload";
        }

        static class Body implements IPostRequest.ISerialize<Map<String, String>> {
            @Nonnull
            private String value1 = "";
            @Nonnull
            private String value2 = "";

            @Nonnull
            @Override
            public Map<String, String> serialize() {
                Map<String, String> map = new HashMap<>();

                map.put("value1", value1);
                map.put("value2", value2);

                return map;
            }

            public void setValue1(@Nonnull String value1) {
                this.value1 = value1;
            }

            public void setValue2(@Nonnull String value2) {
                this.value2 = value2;
            }
        }

        static class Files implements IPostRequest.ISerialize<Map<String, File>> {
            @Nullable
            private File file1;

            @Nonnull
            @Override
            public Map<String, File> serialize() {
                Map<String, File> map = new HashMap<>();

                if (file1 != null) {
                    map.put("file1", file1);
                }

                return map;
            }

            public void setFile1(@Nonnull File file1) {
                this.file1 = file1;
            }
        }
    }

    private static class FakeGetRequest extends AbstractGetRequest {
        @Nullable
        private Params params;

        @Nonnull
        @Override
        public Params getParams() {
            if (params == null) {
                params = new Params();
            }
            return params;
        }

        @Nonnull
        @Override
        public String getResource() {
            return "/fake/get";
        }

        static class Params implements IPostRequest.ISerialize<Map<String, String>> {
            @Nonnull
            private String value1 = "";
            @Nonnull
            private String value2 = "";

            @Nonnull
            @Override
            public Map<String, String> serialize() {
                Map<String, String> map = new HashMap<>();

                map.put("value1", value1);
                map.put("value2", value2);

                return map;
            }

            public void setValue1(@Nonnull String value1) {
                this.value1 = value1;
            }

            public void setValue2(@Nonnull String value2) {
                this.value2 = value2;
            }
        }
    }

    private static class Credentials implements ICredentials {
        private boolean isAuth = true;

        @Nonnull
        @Override
        public String getAuthToken() {
            return isAuth ? "8xampleAuthToke9" : "";
        }

        public void setAuth(boolean auth) {
            isAuth = auth;
        }
    }
}
