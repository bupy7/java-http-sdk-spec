package ru.mihaly4.httpsdkspec.asset.client;

import javax.annotation.Nonnull;

public interface ICredentials {
    @Nonnull
    String getAuthToken();
}
