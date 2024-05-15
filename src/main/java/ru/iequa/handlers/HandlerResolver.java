package ru.iequa.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import ru.iequa.httpserver.ClientsStorage;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

public class HandlerResolver extends HandlerBase {

    HashSet<HandlerBase> handlers = new HashSet<>();

    public HandlerResolver(HashSet<HandlerBase> handlers) {
        this.handlers.addAll(handlers);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println(LocalDateTime.now() + ": " + exchange.getRequestMethod() + " request from address: \""
                + exchange.getRemoteAddress().getAddress()
                + "\" " +
                exchange.getRequestURI());
        final var method = exchange.getRequestMethod();
        final var path = exchange.getRequestURI().getPath();
        try {
            if (method.equals("OPTIONS")) {
                var handlersMethods = handlers.stream()
                        .filter(h -> ("/" + h.getPath()).equals(path))
                        .map(HandlerBase::getMethod)
                        .toList();
                new ResponseCreator().sendCorsPreflightResponse(exchange, handlersMethods);
            }
            var handler = handlers.stream()
                    .filter(h -> ("/" + h.getPath()).equals(path))
                    .filter(h -> h.getMethod().equals(method))
                    .findFirst();
            if (handler.isPresent()) {
                if (handler.get().needsAuth() && !checkAuth(handler.get().needsAuth(), exchange.getRequestHeaders(), exchange.getRemoteAddress().getAddress().toString())) {
                    new ResponseCreator().sendNotAuthorizedResponse(exchange, "Вы не авторизированы либо срок сессии истёк, требуется повторная авторизация");
                    return;
                }
                handler.get().handle(exchange);
            } else {
                throw new IOException("Обработчик не найден");
            }
        } catch (IOException ex) {
            new ResponseCreator().sendResponseWithError(exchange, ex);
        }
    }

    private boolean checkAuth(boolean authNeeded, Headers requestHeaders, String ip) {
        final var token = requestHeaders.get("Token");
        if (token != null && authNeeded) {
            return ClientsStorage.isClientExistsAndValid(UUID.fromString(token.get(0)), ip);
        }
        return false;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public boolean needsAuth() {
        return false;
    }
}
