package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.LoginRequest;
import ru.iequa.httpserver.ClientsStorage;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;
import ru.iequa.utils.TokenHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LogoutHandler extends HandlerBase {

    public static final String PATH = "process-logout";

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
    public boolean needsAuth() {
        return true;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        final LoginRequest request = JsonWorker.getInstance().deserialize(json, LoginRequest.class);
        final var token = TokenHelper.checkAndGetValidUserToken(exchange);
        ClientsStorage.deleteClient(token);
        new ResponseCreator().sendOkResponseWithMessage(exchange, "Успешный выход");
    }
}
