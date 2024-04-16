package ru.iequa.models.db;

import java.util.HashMap;

public class Row {
    private final HashMap<String, Object> elements;

    public Row(HashMap<String, Object> elements) {
        this.elements = new HashMap<>(elements);
    }

    public Object getElement(String colName) {
        if (elements.containsKey(colName)) {
            return elements.get(colName);
        }
        return null;
    }
}
