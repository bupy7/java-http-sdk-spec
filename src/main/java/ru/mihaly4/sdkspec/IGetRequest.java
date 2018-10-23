package ru.mihaly4.sdkspec;

import javax.annotation.Nullable;

public interface IGetRequest extends IRequest {
    @Nullable
    Serialize getParams();
}
