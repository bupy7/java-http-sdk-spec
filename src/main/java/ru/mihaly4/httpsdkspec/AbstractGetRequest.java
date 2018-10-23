package ru.mihaly4.httpsdkspec;

import javax.annotation.Nullable;

public abstract class AbstractGetRequest implements IGetRequest {
    @Nullable
    @Override
    public Serialize getParams() {
        return null;
    }

    @Nullable
    @Override
    public Serialize getHeaders() {
        return null;
    }
}
