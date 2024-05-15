package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.SetNewsDataRequest;
import ru.iequa.database.DB;
import ru.iequa.models.db.SQLTypes;
import ru.iequa.models.db.SqlParams;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

import static ru.iequa.utils.TokenHelper.checkAndGetValidUserToken;

public class SetNewsDataHandler extends HandlerBase {

    public static final String PATH = "set-news-data";

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
        final SetNewsDataRequest request = JsonWorker.getInstance().deserialize(json, SetNewsDataRequest.class);
        final var token = checkAndGetValidUserToken(exchange);
        // менять ли id на изменившего ?? final var userId = ClientsStorage.getClientId(token);
        final var sql = """
                update
                    public.news
                set
                    shorttitle = ?,
                    shortbody = ?,
                    title = ?,
                    body = ?,
                    img = ?,
                    date = ?
                where id = ?""";
        final var params = new ArrayList<SqlParams>();
        final var d = request.data;

        params.add(new SqlParams(SQLTypes.VARCHAR, d.shortTitle, 1));
        params.add(new SqlParams(SQLTypes.VARCHAR, d.shortBody, 2));
        params.add(new SqlParams(SQLTypes.VARCHAR, d.title, 3));
        params.add(new SqlParams(SQLTypes.VARCHAR, d.body, 4));
        if (d.image != null) {
            String imagecl = d.image.replaceFirst("(data:image/png;base64,)|(data:image/jpeg;base64,)", "");
            final var s1 = new ByteArrayInputStream(imagecl.getBytes(StandardCharsets.UTF_8));
            final var s2 = new BufferedInputStream(s1);
            final byte[] image = Base64.getDecoder().wrap(s2).readAllBytes();
            params.add(new SqlParams(SQLTypes.BINARY, image, 5));
        } else {
            params.add(new SqlParams(SQLTypes.BINARY, null, 5));
        }
        params.add(new SqlParams(SQLTypes.TIMESTAMP, d.date, 6));
        params.add(new SqlParams(SQLTypes.INT, d.id, 7));
        final var res = DB.getInstance().ExecQueryWithParams(sql, params);
        if (res == 1) {
            new ResponseCreator().sendOkResponseWithMessage(exchange, "Новость успешно изменена!");
        } else {
            new ResponseCreator().sendResponseWithErrorMessage(exchange, "Ошибка. Новость не удалось изменить");
        }
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
