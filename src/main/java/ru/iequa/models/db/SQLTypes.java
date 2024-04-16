package ru.iequa.models.db;

import java.rmi.NoSuchObjectException;

public enum SQLTypes {
    VARCHAR("varchar"),
    INT("int4"),
    SERIAL("int4"),
    DATE("date"),
    BINARY("bytea");

    private final String value;

    SQLTypes(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SQLTypes typeFromString(String name) throws NoSuchObjectException {
        if (name.equals(INT.value)) {
            return INT;
        }
        if (name.equals(DATE.value)) {
            return DATE;
        }
        if (name.equals(VARCHAR.value)) {
            return VARCHAR;
        }
        if (name.equals(BINARY.value)) {
            return BINARY;
        }
        throw new NoSuchObjectException("Типа " + name + " не существует");
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
