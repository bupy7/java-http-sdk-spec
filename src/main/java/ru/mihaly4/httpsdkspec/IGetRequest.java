package ru.mihaly4.httpsdkspec;

import javax.annotation.Nullable;
import java.util.Map;

public interface IGetRequest extends IRequest {
    @Nullable
    ISerialize<Map<String, String>> getParams();
}
