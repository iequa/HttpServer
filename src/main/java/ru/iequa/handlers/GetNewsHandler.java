package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.Request;
import ru.iequa.contracts.response.NewsPreviewsResponse;
import ru.iequa.database.DB;
import ru.iequa.models.db.DBResult;
import ru.iequa.models.news.NewsPreview;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;

public class GetNewsHandler extends HandlerBase {

    public static final String PATH = "get-news-prevs";

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
        String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Request request = JsonWorker.getInstance().deserialize(json, Request.class);
        DBResult res = DB.getInstance().ExecQuery("select * from news");
        final var rows = res.getRows();
        final var responseList = new ArrayList<NewsPreview>();
        if (!rows.isEmpty()) {
            rows.forEach(row -> {
                final int id = (int) row.getElement("id");
                final String title = (String) row.getElement("shorttitle");
                final String shortBody = (String) row.getElement("shortbody");
                final byte[] prev = (byte[]) row.getElement("img");
                final Timestamp date = (Timestamp) row.getElement("date");
                responseList.add(
                        new NewsPreview(id, title, shortBody, Base64.getEncoder().encodeToString(prev), date)
                );
            });
        }
        new ResponseCreator().sendResponseWithBody(exchange, new NewsPreviewsResponse(responseList));
//        if (ClientsStorage.isClientIdExists(request.id)) {
//            if (request.clientId != null && ClientsStorage.isClientExists(request.id, request.clientId)) {
//                new ResponseCreator().sendNotFoundResponse(exchange, "Данный пользователь уже существует");
//            } else {
//                UUID uuid = UUID.randomUUID();
//                var ip = exchange.getRemoteAddress().getAddress().getHostAddress();
//                ClientsStorage.addClientUUID(request.id, uuid, ip);
//                new ResponseCreator().sendResponseWithBody(exchange, new BaseResponse(uuid.toString(), 200));
//            }
//        } else {
//            new ResponseCreator().sendNotFoundResponse(exchange, "Пользователь не найден");
//        }
    }
}
