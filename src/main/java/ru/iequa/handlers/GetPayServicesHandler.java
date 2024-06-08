package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.Request;
import ru.iequa.contracts.response.ServicesResponse;
import ru.iequa.database.DB;
import ru.iequa.models.db.Row;
import ru.iequa.models.serviceinfo.ServiceInfo;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class GetPayServicesHandler extends HandlerBase {

    public static final String PATH = "get-pay-services";

    public static final String METHOD = "GET";

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

        final var sql = """
                select distinct s.id, s.name, s.type, s.cost
                from
                    public.services s
                where type = 'Платная услуга'
                order by s.name desc;
                """;
        final var res = DB.getInstance().ExecQuery(sql);
        final var rows = res.getRows();
        if (!rows.isEmpty()) {
            final var infoList = new ArrayList<ServiceInfo>();
            for (Row row : rows) {
                final var id = (int) row.getElement("id");
                final var name = (String) row.getElement("name");
                final var type = (String) row.getElement("type");
                final var cost = (String) row.getElement("cost");
                infoList.add(new ServiceInfo(id, name, type, cost, null));
            }
            final var resp = new ServicesResponse(infoList);
            new ResponseCreator().sendResponseWithBody(exchange, resp);
            return;
        }
        throw new IOException("Нет данных об услугах");
    }
}
