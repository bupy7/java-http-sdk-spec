package ru.mihaly4.httpsdkspec;

import javax.annotation.Nullable;

public interface IGetRequest extends IRequest {
    @Nullable
    Serialize getParams();
}
