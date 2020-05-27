package sample.util;


import io.github.mattvass.resultsetmapper.JsonResultSet;

import javax.json.JsonObject;
import java.sql.*;

public class MySqlUtil {
    public static Connection connection;

    private static final String URL = "jdbc:mysql://120.78.131.176:3306/java";
    private static final String USER = "java";
    private static final String PASSWORD = "java";

    public static void openConnection() {
        Connection conn = null;
        try {
            final String DRIVER_NAME = "com.mysql.jdbc.Driver";
            Class.forName(DRIVER_NAME);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("连接成功");
        } catch (ClassNotFoundException e) {
            conn = null;
            System.out.println(e.getException().getMessage());
        } catch (SQLException e) {
            conn = null;
        }

        connection=conn;
    }

    public static JsonObject query(Connection conn, String sql) throws SQLException {
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JsonObject object =new JsonResultSet().toJson(result);

        return object;
    }

    public static void update(Connection conn, String sql) throws SQLException {
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = conn.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insert(Connection conn, String sql) throws SQLException {
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = conn.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean execSQL(Connection conn, String sql) {
        boolean execResult = false;
        if (conn == null) {
            return execResult;
        }

        Statement statement = null;

        try {
            statement = conn.createStatement();
            if (statement != null) {
                execResult = statement.execute(sql);
            }
        } catch (SQLException e) {
            execResult = false;
        }

        return execResult;
    }
}