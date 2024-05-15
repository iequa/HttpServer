package ru.iequa.models.db;

public class SqlParams {
    final public SQLTypes type;
    final public Object object;
    final public int index;

    public SqlParams(SQLTypes type, Object object, int index) {
        this.type = type;
        this.object = object;
        this.index = index;
    }
}
