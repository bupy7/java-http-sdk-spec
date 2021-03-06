package ru.mihaly4.httpsdkspec;

import javax.annotation.Nullable;
import java.util.Map;

public abstract class AbstractGetRequest implements IGetRequest {
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
}
