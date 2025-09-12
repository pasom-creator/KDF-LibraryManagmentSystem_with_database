package ru.home.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static final String URL = "db.url";
    private static final String USERNAME = "db.username";
    private static final String PASSWORD = "db.password";


    private DatabaseConnection() {
        throw new UnsupportedOperationException("Нельзя создать экземпляр утильного класса");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                PropertiesUtil.getProperties(URL),
                PropertiesUtil.getProperties(USERNAME),
                PropertiesUtil.getProperties(PASSWORD)
        );
    }
}
