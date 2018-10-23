package ru.mihaly4.sdkspec;

import java.io.File;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Base interface for any type of request.
 */
public interface IRequest {
    @Nonnull
    String getResource();

    @Nullable
    Serialize getHeaders();

    interface Serialize {
        @Nonnull
        Map<String, String> serialize();
    }

    interface FileSerialize {
        @Nonnull
        Map<String, File> serialize();
    }
}
