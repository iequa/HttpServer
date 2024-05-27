package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.Request;
import ru.iequa.contracts.response.NewsPreviewsResponse;
import ru.iequa.database.DB;
import ru.iequa.models.db.DBResult;
import ru.iequa.models.db.Row;
import ru.iequa.models.news.NewsPreviewData;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDate;
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
        final var offset = request.page != -1 ? request.page * 10 : 0;
        final int countOfPages = (int) Math.ceil(DB.getInstance().ExecNonQuery("select count(*) from news") / 10.0);
        final var ldt = LocalDate.now().plusDays(1);
        final var wp = request.id == 0 ? "" : "where news.date < '" + ldt + "' ";
        DBResult res = DB.getInstance().ExecQuery("select * from news " + wp + "order by news.date DESC offset " + offset);
        final var rows = res.getRows();
        final var responseList = new ArrayList<NewsPreviewData>();
        if (!rows.isEmpty()) {
            for (Row row : rows) {
                final int id = (int) row.getElement("id");
                final String title = (String) row.getElement("shorttitle");
                final String shortBody = (String) row.getElement("shortbody");
                final byte[] prev = (byte[]) row.getElement("img");
                final Timestamp date = (Timestamp) row.getElement("date");
                responseList.add(
                        new NewsPreviewData(id, title, shortBody, prev != null ? Base64.getEncoder().encodeToString(prev) : null, date)
                );
                if (request.page == -1 && responseList.size() == 3) {
                    break;
                }
            }
        }
        new ResponseCreator().sendResponseWithBody(exchange, new NewsPreviewsResponse(responseList, request.page, countOfPages));
    }
}
