package ru.iequa.models.db;

public class Column {
    public final String name;
    public final SQLTypes type;

    public Column(String name, SQLTypes type) {
        this.name = name;
        this.type = type;
    }
}
