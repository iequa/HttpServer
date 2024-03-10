package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.util.UUID;

public class GetUUIDHandler extends HandlerBase {

    public static final String PATH = "get-uuid";

    public static final String METHOD = "GET";

    @Override
    public String getPath() {
        return PATH;
    }

    @Override
    public String getMethod() {
        return METHOD;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String uuid = UUID.randomUUID().toString();
        new ResponseCreator().SendOkResponseWithMessage(exchange, uuid);
    }
}
