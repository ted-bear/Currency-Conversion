package ru.toporkov.util;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public final class ConnectionManager {

    private static final String DB_URL = "bd.url";
    private static final String DB_USER = "bd.user";
    private static final String DB_PASSWORD = "bd.password";

    static {
        loadDriver();
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load SQL driver", e);
        }
    }

    @SneakyThrows
    public static Connection get() {
        return DriverManager.getConnection(
                PropertiesUtil.get(DB_URL),
                PropertiesUtil.get(DB_USER),
                PropertiesUtil.get(DB_PASSWORD)
        );
    }

    private ConnectionManager() {}
}
