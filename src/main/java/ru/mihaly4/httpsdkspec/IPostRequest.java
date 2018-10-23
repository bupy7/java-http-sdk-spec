package ru.mihaly4.httpsdkspec;

import javax.annotation.Nullable;

public interface IPostRequest extends IRequest {
    @Nullable
    Serialize getBody();

    @Nullable
    Serialize getParams();

    @Nullable
    FileSerialize getFiles();
}
