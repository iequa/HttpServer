package ru.iequa.utils;


import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.Response;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class ResponseCreator {

    private final static String ENCODING = "UTF-8";

    public ResponseCreator() {
    }

    public void SendResponse(HttpExchange exchange, Response response) throws IOException {
        String body = JsonWorker.GetInstance().Serialize(response);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=" + ENCODING);
        exchange.sendResponseHeaders(response.code, body.getBytes(ENCODING).length);
        //exchange.setAttribute("Content-Type", "text/plain");
        OutputStreamWriter osw = new OutputStreamWriter(exchange.getResponseBody(), ENCODING);
        osw.write(body);
        osw.close();
    }

    public void SendOkResponseWithObject(HttpExchange exchange, Response responseObject) throws IOException {
        this.SendResponse(exchange, responseObject);
    }

    public void SendOkResponseWithMessage(HttpExchange exchange, String message) throws IOException {
        var resp = new Response(
                null, message, 200, null);
        this.SendResponse(exchange, resp);
    }

    public void SendEmptyOkResponse(HttpExchange exchange) throws IOException {
        var resp = new Response(
                null, "OK", 200, null);
        this.SendResponse(exchange, resp);
    }

    public void SendNotFoundResponse(HttpExchange exchange) throws IOException {
        var resp = new Response(
                null, "Not found =(", 404, null);
        this.SendResponse(exchange, resp);
    }

    public void SendNotFoundResponse(HttpExchange exchange, String message) throws IOException {
        var resp = new Response(
                null, JsonWorker.GetInstance().Serialize(message), 404, null);
        this.SendResponse(exchange, resp);
    }
}
