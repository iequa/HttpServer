package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.Request;
import ru.iequa.contracts.response.UserDataResponse;
import ru.iequa.database.DB;
import ru.iequa.httpserver.ClientsStorage;
import ru.iequa.models.db.Row;
import ru.iequa.models.serviceinfo.ServiceInfo;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

public class GetUserDataHandler extends HandlerBase {

    public static final String PATH = "get-user-data";

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
        return true;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Request request = JsonWorker.getInstance().deserialize(json, Request.class);
        final var token = exchange.getRequestHeaders().get("Token");
        if (token != null && ClientsStorage.isClientUUIDExists(UUID.fromString(token.get(0)))) {

            final var sql = """
                    select distinct u.id, u.login, u.is_admin, ud.name, ud.surname, ud.gender, s.name sname, s.type, s.cost, us.provision_date
                    from
                        public.users u
                        right join public.user_data ud on ud.id = u.id
                        right join public.user_services us on us.user_id = u.id
                        right join public.services s on s.id = us.service_id
                    where
                        u.id = :id
                    """
                    .replace(":id", ClientsStorage.getClientId(UUID.fromString(token.get(0))));
            final var res = DB.getInstance().ExecQuery(sql);
            final var rows = res.getRows();
            if (!rows.isEmpty()) {
                final var infoList = new ArrayList<ServiceInfo>();
                final var rowBaseInfo = rows.stream().findAny().get();
                final var login = (String) rowBaseInfo.getElement("login");
                final var surname = (String) rowBaseInfo.getElement("surname");
                final var name = (String) rowBaseInfo.getElement("name");
                final var gender = (String) rowBaseInfo.getElement("gender");
                for (Row row : rows) {
                    final String sname = (String) row.getElement("sname");
                    final String type = (String) row.getElement("type");
                    final String cost = row.getElement("cost") == null ? null : (String) row.getElement("cost");
                    final String provision_date = row.getElement("provision_date").toString();
                    infoList.add(new ServiceInfo(sname, type, cost, provision_date.substring(0, provision_date.length() - 2)));
                }
                final var resp = new UserDataResponse(login, surname, name, gender, infoList);
                new ResponseCreator().sendResponseWithBody(exchange, resp);
                return;
            }
            throw new IOException("Нет данных о пользователе");
        }
        new ResponseCreator().sendOkResponseWithMessage(exchange, "it worked");
    }
}
