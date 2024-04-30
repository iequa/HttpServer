package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.util.HashSet;

public class HandlerResolver extends HandlerBase {

    HashSet<HandlerBase> handlers = new HashSet<>();

    public HandlerResolver(HashSet<HandlerBase> handlers) {
        this.handlers.addAll(handlers);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getRequestMethod() + " request from \""
                + " address: \""
                + exchange.getRemoteAddress().getAddress()
                + "\" " +
                exchange.getRequestURI());
        var method = exchange.getRequestMethod();
        var path = exchange.getRequestURI().getPath();
        if (method.equals("OPTIONS")) {
            var handlersMethods = handlers.stream()
                    .filter(h -> ("/" + h.getPath()).equals(path))
                    .map(HandlerBase::getMethod)
                    .toList();
            new ResponseCreator().sendCorsPreflightResponse(exchange, handlersMethods);
        }
        try {
            var handler = handlers.stream()
                    .filter(h -> ("/" + h.getPath()).equals(path))
                    .filter(h -> h.getMethod().equals(method))
                    .findFirst();
            if (handler.isPresent()) {
                handler.get().handle(exchange);
            } else {
                throw new ClassNotFoundException("Обработчик не найден");
            }
        } catch (Exception ex) {
            new ResponseCreator().sendNotFoundResponse(exchange, ex.getMessage());
        }
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String getMethod() {
        return null;
    }
}
