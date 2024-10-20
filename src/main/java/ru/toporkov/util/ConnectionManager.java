package ru.toporkov.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class ConnectionManager {

    private static final String DB_URL_KEY = "db.url";
    private static final String DB_USER_KEY = "db.user";
    private static final String DB_PASSWORD_KEY = "db.password";
    private static final String DB_DRIVER_KEY = "db.driver";
    private static final String DB_POOL_SIZE_KEY = "db.pool.size";
    private static final Integer DB_DEFAULT_POOL_SIZE = 10;
    private static BlockingQueue<Connection> connectionPoolQueue;
    private static List<Connection> sourceConnections;

    static {
        loadDriver();
        initConnectionPool();
    }

    private static void initConnectionPool() {
        var poolSizeFromPropertyFile = PropertiesUtil.get(DB_POOL_SIZE_KEY);
        var initializingSizeOfPool = poolSizeFromPropertyFile == null ? DB_DEFAULT_POOL_SIZE : Integer.parseInt(poolSizeFromPropertyFile);
        connectionPoolQueue = new ArrayBlockingQueue<>(initializingSizeOfPool);
        sourceConnections = new ArrayList<>(initializingSizeOfPool);

        for (int i = 0; i < initializingSizeOfPool; i++) {
            var connection = open();
            var proxyConnection = (Connection) Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close") ? pool.add((Connection) proxy) : method.invoke(connection, args));
            connectionPoolQueue.add(proxyConnection);
            sourceConnections.add(connection);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName(PropertiesUtil.get(DB_DRIVER_KEY));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load SQL driver", e);
        }
    }

    private static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(DB_URL_KEY),
                    PropertiesUtil.get(DB_USER_KEY),
                    PropertiesUtil.get(DB_PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection get() {
        try {
            return connectionPoolQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closePool() {
        try {
            for (Connection connection : sourceConnections) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during closing connection pool", e);
        }
    }

    private ConnectionManager() {}
}
