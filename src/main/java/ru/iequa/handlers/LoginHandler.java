package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.LoginRequest;
import ru.iequa.contracts.response.LoginResponse;
import ru.iequa.database.DB;
import ru.iequa.httpserver.ClientsStorage;
import ru.iequa.models.db.DBResult;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

public class LoginHandler extends HandlerBase {

    public static final String PATH = "process-login";

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
        final LoginRequest request = JsonWorker.getInstance().deserialize(json, LoginRequest.class);
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        }
        final byte[] encodedHash = digest.digest(request.value.getBytes(StandardCharsets.UTF_8));
        var res = bytesToHex(encodedHash);
        final var count = DB.getInstance().ExecQuery("select u.id from public.users u where u.login = ':l' and u.password = ':p'"
                .replace(":l", request.login)
                .replace(":p", res));
        if (!count.getRows().isEmpty()) {
            final var id = count.getRows().stream().map(row -> row.getElement("id")).findFirst().get();
            final int typeOfUser = DB.getInstance().ExecNonQuery("select count (*) from public.users u where u.id = " + id + " and u.is_admin = true");
            final var userData = DB.getInstance().ExecQuery("select ud.name from public.user_data ud where ud.user_id = " + id);
            final var row = userData.getRows().stream().toList().get(0);
            final var name = (String) row.getElement("name");
            final var token = UUID.randomUUID();
            ClientsStorage.addClient(
                    id.toString(),
                    token,
                    exchange.getRemoteAddress().getAddress().toString(),
                    LocalDateTime.now().plusMinutes(10),
                    typeOfUser == 1
            );
            exchange.getResponseHeaders().set("Token", token.toString());
            DBResult availableDate = DB.getInstance().ExecQuery("select ud.next_donation_date from public.user_data ud where ud.user_id = " + ClientsStorage.getClientId(token));
            final String ad = availableDate.getRowByIndex(0).getElement("next_donation_date") != null ?
                    availableDate.getRowByIndex(0).getElement("next_donation_date").toString()
                    :
                    null;
            new ResponseCreator().sendResponseWithBody(
                    exchange,
                    new LoginResponse(null,
                            "Успешная авторизация",
                            200,
                            token.toString(),
                            name,
                            ad,
                            ClientsStorage.isClientHasAdminPermissions(token)
                    )
            );
            return;
        }
        new ResponseCreator().sendNotFoundResponse(exchange, "Пользователь не найден");
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
