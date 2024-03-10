package ru.iequa.utils;

import com.google.gson.Gson;

public final class JsonWorker {
    private static JsonWorker instance = new JsonWorker();
    private static Gson gson;

    private JsonWorker() {
        gson = new Gson();
    }

    public static JsonWorker GetInstance() {
        return instance;
    }

    public String Serialize(Object object) {
        return gson.toJson(object);
    }

    public <T> T Deserialize(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }
}
