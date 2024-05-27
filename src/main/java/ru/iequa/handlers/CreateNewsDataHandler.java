package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.SetNewsDataRequest;
import ru.iequa.contracts.response.SetNewsDataResponse;
import ru.iequa.database.DB;
import ru.iequa.httpserver.ClientsStorage;
import ru.iequa.models.db.SQLTypes;
import ru.iequa.models.db.SqlParams;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;

import static ru.iequa.utils.TokenHelper.checkAndGetValidUserToken;

public class CreateNewsDataHandler extends HandlerBase {

    public static final String PATH = "create-news";

    public static final String METHOD = "PUT";

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
        final var sql = """
                insert into
                    public.news (body, shorttitle, title, author, img, shortbody, "date")
                values(
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?)
                    RETURNING id""";
        final var params = new ArrayList<SqlParams>();
        final var d = request.data;

        params.add(new SqlParams(SQLTypes.VARCHAR, d.body, 1));
        params.add(new SqlParams(SQLTypes.VARCHAR, d.shortTitle, 2));
        params.add(new SqlParams(SQLTypes.VARCHAR, d.title, 3));
        params.add(new SqlParams(SQLTypes.INT, Integer.parseInt(ClientsStorage.getClientId(token)), 4));

        if (d.image != null) {
            String imagecl = d.image.replaceFirst("(data:image/png;base64,)|(data:image/jpeg;base64,)", "");
            final var s1 = new ByteArrayInputStream(imagecl.getBytes(StandardCharsets.UTF_8));
            final var s2 = new BufferedInputStream(s1);
            final byte[] image = Base64.getDecoder().wrap(s2).readAllBytes();
            params.add(new SqlParams(SQLTypes.BINARY, image, 5));
        } else {
            params.add(new SqlParams(SQLTypes.BINARY, null, 5));
        }
        params.add(new SqlParams(SQLTypes.VARCHAR, d.shortBody, 6));
        params.add(new SqlParams(SQLTypes.DATE, new Date(d.date.getTime()), 7));
        final var res = DB.getInstance().ExecQueryWithParamsWithReturn(sql, params);
        if (res != null && !res.getRows().isEmpty()) {
            new ResponseCreator().sendResponseWithBody(exchange,
                    new SetNewsDataResponse(null,
                            "Новость успешно добавлена!",
                            200,
                            (int) res.getRowByIndex(0).getElement("id"))
            );
        } else {
            new ResponseCreator().sendResponseWithErrorMessage(exchange, "Ошибка. Новость не удалось создать");
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
