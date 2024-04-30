package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.Request;
import ru.iequa.contracts.request.SetCalendarDatetimeRequest;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SetCalendarDatetimeHandler extends HandlerBase {

    public static final String PATH = "set-service-provision-date";

    public static final String METHOD = "PUT";

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
        Request request = JsonWorker.getInstance().deserialize(json, SetCalendarDatetimeRequest.class);
        new ResponseCreator().sendOkResponseWithMessage(exchange, "it worked");
    }
}
