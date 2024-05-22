package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.RegisterRequest;
import ru.iequa.database.DB;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterHandler extends HandlerBase {

    public static final String PATH = "process-register";

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
        return false;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        final RegisterRequest request = JsonWorker.getInstance().deserialize(json, RegisterRequest.class);
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        }
        final byte[] encodedHash = digest.digest(request.value.getBytes(StandardCharsets.UTF_8));
        final var res = bytesToHex(encodedHash);
        final var count = DB.getInstance().ExecQuery("select count(*) from public.users u where u.login = '" + request.login + "'");
        if (count.getRows().isEmpty()) {
            final var sql = """
                    with rows as(
                        INSERT INTO
                            public.users(login, "password", is_admin)
                        VALUES
                            (':log', ':pass', false)
                        RETURNING id
                    )
                    INSERT INTO public.user_data
                        ("name", surname, user_id, birthdate)
                    VALUES
                        (':name', ':surname', (select id from rows), ':birthdate');
                    """.replace(":log", request.login)
                    .replace(":pass", res)
                    .replace(":name", request.name)
                    .replace(":surname", request.surname)
                    .replace(":birthdate", request.date);
            final var resOfReg = DB.getInstance().ExecInsertOrUpdate(sql);
            if (resOfReg == 1) {
                new ResponseCreator().sendOkResponseWithMessage(exchange, "Успешная регистрация! Войдите с помощью своих данных.");
            } else {
                new ResponseCreator().sendResponseWithErrorMessage(exchange, "Во время регистрации произошла ошибка, проверьте вводимые данные.");
            }
            return;
        }
        new ResponseCreator().sendNotFoundResponse(exchange, "Пользователь с таким логином уже существует");
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
