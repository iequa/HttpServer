package ru.iequa.utils;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.httpserver.ClientsStorage;

import java.io.IOException;
import java.util.UUID;

public class TokenHelper {
    public static UUID checkAndGetValidUserToken(HttpExchange exchange) throws IOException {
        final var token = exchange.getRequestHeaders().get("Token");
        if (token == null) {
            throw new IOException("Вы не авторизированы либо срок сессии истёк, требуется повторная авторизация");
        }
        if (token.isEmpty()) {
            throw new IOException("Вы не авторизированы либо срок сессии истёк, требуется повторная авторизация");
        }
        final var tokenValue = token.get(0);
        if (tokenValue.length() != 36) {
            throw new IOException("Вы не авторизированы либо срок сессии истёк, требуется повторная авторизация");
        }
        if (ClientsStorage.isClientExistsAndValid(
                UUID.fromString(tokenValue),
                exchange.getRemoteAddress().getAddress().toString()
        )) {
            return UUID.fromString(tokenValue);
        }
        throw new IOException("Вы не авторизированы либо срок сессии истёк, требуется повторная авторизация");
    }

    public static String checkAndGetValidUserTokenStr(HttpExchange exchange) throws IOException {
        final var token = exchange.getRequestHeaders().get("Token");
        if (token == null) {
            throw new IOException("Вы не авторизированы либо срок сессии истёк, требуется повторная авторизация");
        }
        if (token.isEmpty()) {
            throw new IOException("Вы не авторизированы либо срок сессии истёк, требуется повторная авторизация");
        }
        final var tokenValue = token.get(0);
        if (tokenValue.length() != 36) {
            throw new IOException("Вы не авторизированы либо срок сессии истёк, требуется повторная авторизация");
        }
        if (ClientsStorage.isClientExistsAndValid(
                UUID.fromString(tokenValue),
                exchange.getRemoteAddress().getAddress().toString()
        )) {
            return tokenValue;
        }
        return null;
    }
}
