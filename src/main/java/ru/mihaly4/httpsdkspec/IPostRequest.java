package ru.mihaly4.httpsdkspec;

import javax.annotation.Nullable;
import java.util.Map;

public interface IPostRequest<T> extends IRequest {
    @Nullable
    ISerialize<T> getBody();

    @Nullable
    ISerialize<Map<String, String>> getParams();
}
