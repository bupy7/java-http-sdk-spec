package ru.mihaly4.httpsdkspec;

import javax.annotation.Nullable;
import java.util.Map;

public abstract class AbstractFormPostRequest implements IFormPostRequest {
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
}
