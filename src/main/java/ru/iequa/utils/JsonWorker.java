package ru.iequa.utils;

import com.google.gson.Gson;

public final class JsonWorker {
    private static final JsonWorker instance = new JsonWorker();
    private static Gson gson;

    private JsonWorker() {
        gson = new Gson();
    }

    public static JsonWorker getInstance() {
        return instance;
    }

    public String serialize(Object object) {
        return gson.toJson(object);
    }

    public <T> T deserialize(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }
}
