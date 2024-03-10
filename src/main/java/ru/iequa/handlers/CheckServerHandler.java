package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.Request;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CheckServerHandler extends HandlerBase {

    public static final String PATH = "check";

    public static final String METHOD = "POST";

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
        String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Request request = JsonWorker.GetInstance().Deserialize(json, Request.class);

        new ResponseCreator().SendEmptyOkResponse(exchange);
    }
}
