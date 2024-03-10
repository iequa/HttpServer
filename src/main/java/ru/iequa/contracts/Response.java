package ru.iequa.contracts;

public class Response {
    public final String uuid;
    public final String message;
    public final int code;

    public final String body;

    public Response(String uuid, String message, int code, String body) {
        this.uuid = uuid;
        this.message = message;
        this.code = code;
        this.body = body;
    }
}

