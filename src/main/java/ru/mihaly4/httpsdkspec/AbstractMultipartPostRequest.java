package ru.mihaly4.httpsdkspec;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Map;

public abstract class AbstractMultipartPostRequest implements IMultipartPostRequest {
    @Nullable
    @Override
    public ISerialize<Map<String, String>> getParams() {
        return null;
    }

    @Nullable
    @Override
    public ISerialize<Map<String, String>> getHeaders() {
        return null;
    }

    @Nullable
    @Override
    public ISerialize<Map<String, String>> getBody() {
        return null;
    }

    @Nullable
    @Override
    public ISerialize<Map<String, File>> getFiles() {
        return null;
    }
}
