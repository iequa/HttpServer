package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public abstract class HandlerBase implements HttpHandler {

    public abstract String getPath();

    public abstract String getMethod();

    @Override
    public abstract void handle(HttpExchange exchange) throws IOException;
}
