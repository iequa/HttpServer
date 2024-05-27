package ru.iequa.utils;


import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.response.RecordsReportResponse;
import ru.iequa.contracts.response.base.BaseResponse;
import ru.iequa.contracts.response.base.ResponseBase;
import ru.iequa.httpserver.ClientsStorage;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.UUID;

public class ResponseCreator {

    private final static String ENCODING = "UTF-8";

    public ResponseCreator() {
    }

    public void sendResponseWithBody(HttpExchange exchange, ResponseBase response) throws IOException {
        String body = JsonWorker.getInstance().serialize(response);
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Token");
        exchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Authorization");
        if (response.getClass().equals(RecordsReportResponse.class)) {
            exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
            exchange.getResponseHeaders().set("Content-Disposition", "attachment; filename=\"myfile-1.xlsx\"");
        } else {
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=" + ENCODING);
        }
        final var tokenObj = exchange.getRequestHeaders().get("Token");
        if (tokenObj != null && ClientsStorage.isClientUUIDExists(UUID.fromString(tokenObj.get(0)))) {
            exchange.getResponseHeaders().set("Token", tokenObj.get(0));
        }
        exchange.sendResponseHeaders(response.getCode(), body.getBytes(ENCODING).length);
        exchange.setAttribute("body", body);
        //exchange.setAttribute("Content-Type", "text/plain");
        OutputStreamWriter osw = new OutputStreamWriter(exchange.getResponseBody(), ENCODING);
        osw.write(body);
        osw.close();
    }

    public void sendEmptyOkResponse(HttpExchange exchange) throws IOException {
        final var resp = new BaseResponse(null, 200);
        this.sendResponseWithBody(exchange, resp);
    }

    public void sendOkResponseWithMessage(HttpExchange exchange, String message) throws IOException {
        final var resp = new BaseResponse(null, message, 200);
        this.sendResponseWithBody(exchange, resp);
    }

    public void sendNotFoundResponse(HttpExchange exchange) throws IOException {
        final var resp = new BaseResponse("Not found =(", 404);
        this.sendResponseWithBody(exchange, resp);
    }

    public void sendResponseWithError(HttpExchange exchange, Exception ex) throws IOException {
        final var resp = new BaseResponse(ex.getMessage(), 400);
        this.sendResponseWithBody(exchange, resp);
    }

    public void sendResponseWithErrorMessage(HttpExchange exchange, String errorMessage) throws IOException {
        final var resp = new BaseResponse(errorMessage, 400);
        this.sendResponseWithBody(exchange, resp);
    }

    public void sendResponseWithCode(HttpExchange exchange, int code, String errorMessage) throws IOException {
        final var resp = new BaseResponse(errorMessage, code);
        this.sendResponseWithBody(exchange, resp);
    }

    public void sendNotFoundResponse(HttpExchange exchange, String errorMessage) throws IOException {
        final var resp = new BaseResponse(errorMessage, 404);
        this.sendResponseWithBody(exchange, resp);
    }

    public void sendNotAuthorizedResponse(HttpExchange exchange, String errorMessage) throws IOException {
        final var resp = new BaseResponse(errorMessage, 401);
        this.sendResponseWithBody(exchange, resp);
    }

    public void sendOkResponseWithBody(HttpExchange exchange, ResponseBase resp) throws IOException {
        this.sendResponseWithBody(exchange, resp);
    }

    public void sendCorsPreflightResponse(HttpExchange exchange, List<String> methods) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", String.join(",", methods));
        this.sendEmptyOkResponse(exchange);
    }

}
