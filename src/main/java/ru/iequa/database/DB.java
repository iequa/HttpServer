package ru.iequa.database;


import ru.iequa.models.db.DBResult;

import java.sql.*;

public class DB {
    private static DB instance;
    public boolean connected;
    private final static String CONN_LOGIN = "iequa";
    private final static String CONN_PASS = "postgres";
    //private final static String CONN_URL = "jdbc:sqlanywhere:kursbd";
    private final static String CONN_URL = "jdbc:postgresql://localhost:5432/vkr";

    public Statement stat;
    private static DatabaseMetaData meta;
    private static Connection con;

    private DB() {
        tryConn();
    }

    public static synchronized DB getInstance() {
        if (instance == null) {
            instance = new DB();
        }
        if (!instance.connected) {
            instance.tryConn();
        }
        return instance;
    }

    public boolean tryConn() {
        if (connected) {
            return true;
        }
        try {
            con = DriverManager.getConnection(CONN_URL, CONN_LOGIN, CONN_PASS);
            meta = con.getMetaData();
            stat = con.createStatement();
            System.out.println("DB connected.");
            connected = true;
            DriverManager.setLoginTimeout(10000);
            return true;
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex.getMessage());
            connected = false;
            return (false);
        }
    }

    public DBResult ExecQuery(String sql) {
        try {
            ResultSet rawRes = stat.executeQuery(sql);
            return new DBResult(rawRes);
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex.getMessage());
            connected = false;
            return null;
        }
    }

    public boolean dbDisconnect() {
        try {
            if (!connected) {
                return true;
            }
            connected = false;
            stat.close();
            con.close();
            System.out.println("Database disconnected");
            return true;
        } catch (SQLException e) {
            System.out.println("Error in DBDisconnect");
            System.out.println(e.getMessage());
            return false;
        }
    }
}
