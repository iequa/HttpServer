package ru.iequa.models.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class DBResult {

    private final LinkedHashSet<Row> rows;
    private final LinkedHashSet<Column> columns;

    public DBResult(ResultSet data) throws SQLException {
        columns = new LinkedHashSet<>();
        rows = new LinkedHashSet<>();
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

    public LinkedHashSet<Row> getRows() {
        return new LinkedHashSet<>(rows);
    }

    public LinkedHashSet<Column> getColumns() {
        return new LinkedHashSet<>(columns);
    }

    public Row getRowByIndex(int index) {
        return rows.stream().toList().get(index);
    }
}
