package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.LoginRequest;
import ru.iequa.httpserver.ClientsStorage;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

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
        final var token = exchange.getRequestHeaders().get("Token");
        ClientsStorage.deleteClient(UUID.fromString(token.get(0)));
        new ResponseCreator().sendOkResponseWithMessage(exchange, "Успешный выход");
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
