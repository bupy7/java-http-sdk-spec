package ru.mihaly4.httpsdkspec;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Map;

public interface IMultipartPostRequest extends IPostRequest<Map<String, String>> {
    @Nullable
    ISerialize<Map<String, File>> getFiles();
}
