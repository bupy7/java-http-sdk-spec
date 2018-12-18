package ru.mihaly4.httpsdkspec.test;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.mihaly4.httpsdkspec.IGetRequest;
import ru.mihaly4.httpsdkspec.IPostRequest;

public class HttpClient {
    private static final String HEADER_AUTH_TOKEN = "Example-Auth-Token";

    @Nonnull
    private ICredentials credentials;
    @Nonnull
    private String entryPoint;
    @Nullable
    private OkHttpClient instance;

    public HttpClient(
            @Nonnull ICredentials credentials,
            @Nonnull String entryPoint
    ) {
        this.credentials = credentials;
        this.entryPoint = entryPoint;
    }

    @Nullable
    public Response sendPost(@Nonnull IPostRequest request) {
        // body data
        RequestBody requestBody;
        if (request.getFiles() == null) {
            FormBody.Builder bodyBuilder = new FormBody.Builder();

            if (request.getBody() != null) {
                for (Map.Entry<String, String> entry : request.getBody().serialize().entrySet()) {
                    bodyBuilder.add(entry.getKey(), entry.getValue());
                }
            }

            requestBody = bodyBuilder.build();
        } else {
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
            bodyBuilder.setType(MultipartBody.FORM);

            for (Map.Entry<String, File> entry : request.getFiles().serialize().entrySet()) {
                File file = entry.getValue();

                String mimeType;
                try {
                    URLConnection con = file.toURI().toURL().openConnection();
                    mimeType = con.getContentType();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                if (mimeType == null) {
                    return null;
                }

                bodyBuilder.addFormDataPart(
                        entry.getKey(),
                        file.getName(),
                        RequestBody.create(MediaType.parse(mimeType), file)
                );
            }
            if (request.getBody() != null) {
                for (Map.Entry<String, String> entry : request.getBody().serialize().entrySet()) {
                    bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                }
            }

            requestBody = bodyBuilder.build();
        }

        // http request
        String url = createUrl(
                request.getResource(),
                entryPoint,
                request.getParams() != null ? request.getParams().serialize() : null
        );
        if (url.isEmpty()) {
            return null;
        }

        Request httpRequest = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        return execute(httpRequest);
    }

    @Nullable
    public Response sendGet(@Nonnull IGetRequest request) {
        String url = createUrl(
                request.getResource(),
                entryPoint,
                request.getParams() != null ? request.getParams().serialize() : null
        );
        if (url.isEmpty()) {
            return null;
        }

        Request httpRequest = new Request.Builder().url(url).build();

        return execute(httpRequest);
    }

    @Nullable
    private Response execute(Request httpRequest) {
        try {
            return getInstance().newCall(httpRequest).execute();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nonnull
    private String createUrl(@Nonnull String action, @Nonnull String entryPoint, @Nullable Map<String, String> params) {
        HttpUrl baseUrl = HttpUrl.parse(entryPoint);
        if (baseUrl == null) {
            return "";
        }

        HttpUrl.Builder builder = baseUrl.newBuilder();

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        return builder.addPathSegments(action.replaceFirst("^/", "")).toString();
    }

    @Nonnull
    private synchronized OkHttpClient getInstance() {
        if (instance == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.addNetworkInterceptor(new AuthInterceptor());

            instance = client.build();
        }
        return instance;
    }

    private class AuthInterceptor implements Interceptor {
        @Nonnull
        @Override
        public Response intercept(@Nonnull Chain chain) throws IOException {
            Request request = chain.request();
            if (!credentials.getAuthToken().isEmpty()) {
                request = request.newBuilder()
                        .addHeader(HEADER_AUTH_TOKEN, credentials.getAuthToken())
                        .build();
            }
            return chain.proceed(request);
        }
    }
}
