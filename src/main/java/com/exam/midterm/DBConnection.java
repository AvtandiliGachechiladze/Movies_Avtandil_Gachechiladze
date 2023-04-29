package com.exam.midterm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/MovieDB";
    /* use this in case of any problems with GlassFish server
    private static final String URL = "jdbc:mysql://localhost:3306/socialposts?useSSL=false&allowPublicKeyRetrieval=true";
    */
    private static final String USERNAME = "root";
    private static final String PASSWORD = "pwd";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL driver", e);
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
