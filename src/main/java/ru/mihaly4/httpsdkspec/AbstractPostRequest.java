package ru.mihaly4.httpsdkspec;

import javax.annotation.Nullable;

public abstract class AbstractPostRequest implements IPostRequest {
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

    @Nullable
    @Override
    public Serialize getBody() {
        return null;
    }

    @Nullable
    @Override
    public FileSerialize getFiles() {
        return null;
    }
}
