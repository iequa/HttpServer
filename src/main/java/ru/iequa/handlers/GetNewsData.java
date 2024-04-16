package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.Request;
import ru.iequa.contracts.response.NewsDataResponse;
import ru.iequa.database.DB;
import ru.iequa.models.db.DBResult;
import ru.iequa.models.db.Row;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;

public class GetNewsData extends HandlerBase {

    public static final String PATH = "get-news-data";

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
    public void handle(HttpExchange exchange) throws IOException {
        String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Request request = JsonWorker.getInstance().deserialize(json, Request.class);
        DBResult res = DB.getInstance().ExecQuery("select * from news where id= " + request.id);
        final var rows = res.getRows();
        if (!rows.isEmpty()) {
            for (Row row : rows) {
                final int id = (int) row.getElement("id");
                final String title = (String) row.getElement("title");
                final String body = (String) row.getElement("body");
                final byte[] prev = (byte[]) row.getElement("img");
                new ResponseCreator().sendResponseWithBody(
                        exchange,
                        new NewsDataResponse(
                                id,
                                title,
                                body,
                                Timestamp.valueOf(LocalDateTime.now()),
                                Base64.getEncoder().encodeToString(prev)
                        )
                );
                break;
            }
        }
        new ResponseCreator().sendResponseWithCode(exchange, 400, "Такой новости не существует!");
    }
}
