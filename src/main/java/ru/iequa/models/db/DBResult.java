package ru.iequa.models.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

public class DBResult {

    private final HashSet<Row> rows;
    private final HashSet<Column> columns;

    public DBResult(ResultSet data) throws SQLException {
        columns = new HashSet<>();
        rows = new HashSet<>();
        ResultSetMetaData rsmd = data.getMetaData();
        try {
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String typename = rsmd.getColumnTypeName(i);
                if (typename.equals("serial")) {
                    columns.add(new Column(rsmd.getColumnName(i), SQLTypes.typeFromString("int4")));
                } else {
                    columns.add(new Column(rsmd.getColumnName(i), SQLTypes.typeFromString(typename)));
                }
            }
            while (data.next()) {
                HashMap<String, Object> values = new HashMap<>();
                columns.forEach((col) -> {
                    try {
                        switch (col.type) {
                            case INT, SERIAL -> values.put(col.name, data.getInt(col.name));
                            case DATE -> values.put(col.name, data.getDate(col.name));
                            case TIMESTAMP -> values.put(col.name, data.getTimestamp(col.name));
                            case BINARY -> values.put(col.name, data.getBytes(col.name));
                            case VARCHAR -> values.put(col.name, data.getString(col.name));
                            case BOOL -> values.put(col.name, data.getBoolean(col.name));
                        }
                    } catch (SQLException e) {
                        values.put(col.name, null);
                    }

                });
                var row = new Row(values);
                rows.add(row);
            }
        } catch (Exception ex) {
            System.out.println("Ошибка получения данных: " + ex.getMessage());
        }
    }

    public HashSet<Row> getRows() {
        return new HashSet<>(rows);
    }
}
