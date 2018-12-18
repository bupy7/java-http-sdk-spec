package ru.mihaly4.httpsdkspec.test;

import javax.annotation.Nonnull;

public interface ICredentials {
    @Nonnull
    String getAuthToken();
}
