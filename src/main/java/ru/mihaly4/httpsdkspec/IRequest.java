package ru.mihaly4.httpsdkspec;

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
    ISerialize<Map<String, String>> getHeaders();

    interface ISerialize<T> {
        @Nonnull
        T serialize();
    }
}