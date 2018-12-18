package ru.mihaly4.httpsdkspec.test;

public class Env {
    public static int getFakeServerPort() {
        String port = System.getenv("RU_MIHALY4_HTTPSDKSPEC_FAKE_SERVER_PORT");
        if (port != null) {
            return Integer.valueOf(port);
        }
        return 10987;
    }
}
