package ru.iequa.database;


import ru.iequa.models.db.DBResult;
import ru.iequa.models.db.SqlParams;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class DB {
    private static DB instance;
    public boolean connected;
    private final static String CONN_LOGIN = "iequa";
    private final static String CONN_PASS = "postgres";
    //private final static String CONN_URL = "jdbc:sqlanywhere:kursbd";
    private final static String CONN_URL = "jdbc:postgresql://localhost:5432/vkr";

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
            System.out.println("DB connected.");
            connected = true;
            DriverManager.setLoginTimeout(10000);
            return true;
        } catch (Exception ex) {
            System.out.println("Error...");
            System.out.println(ex.getMessage());
            connected = false;
            return (false);
        }
    }

    public DBResult ExecQuery(String sql) {
        try {
            final ResultSet rawRes = con.prepareStatement(sql).executeQuery();
            return new DBResult(rawRes);
        } catch (Exception ex) {
            System.out.println("Error...");
            System.out.println(ex.getMessage());
            connected = false;
            return null;
        }
    }

    public int ExecQueryWithParams(String sql, List<SqlParams> params) {
        try {
            final var stat = fillStatementWithParams(con.prepareStatement(sql), params);
            return stat.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error...");
            System.out.println(ex.getMessage());
            connected = false;
            return -1;
        }
    }

    public int ExecInsertOrUpdate(String sql) throws IOException {
        try {
            return con.prepareStatement(sql).executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error...");
            System.out.println(ex.getMessage());
            connected = false;
            throw new IOException(ex.getMessage());
        }
    }

    public int ExecNonQuery(String sql) {
        try {
            var res = con.prepareStatement(sql).executeQuery();
            res.next();
            return res.getInt(1);
        } catch (Exception ex) {
            System.out.println("Error...");
            System.out.println(ex.getMessage());
            connected = false;
            return 0;
        }
    }

    private PreparedStatement fillStatementWithParams(PreparedStatement stat, List<SqlParams> params) throws IOException {
        try {
            for (SqlParams parameter : params) {
                switch (parameter.type) {
                    case INT -> stat.setInt(parameter.index, (int) parameter.object);
                    case VARCHAR -> stat.setString(parameter.index, (String) parameter.object);
                    case DATE -> stat.setDate(parameter.index, (Date) parameter.object);
                    case TIMESTAMP -> stat.setTimestamp(parameter.index, (Timestamp) parameter.object);
                    case BINARY -> {
                        if (parameter.object != null) {
                            stat.setBinaryStream(parameter.index, new ByteArrayInputStream((byte[]) parameter.object));
                        } else {
                            stat.setNull(parameter.index, Types.BINARY);
                        }
                    }
                    case BOOL -> stat.setBoolean(parameter.index, (boolean) parameter.object);
                    default -> throw new SQLException("Неверный тип");
                }
            }
            return stat;
        } catch (SQLException ex) {
            throw new IOException(ex.getLocalizedMessage());
        }
    }

    public boolean dbDisconnect() {
        try {
            if (!connected) {
                return true;
            }
            connected = false;
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
