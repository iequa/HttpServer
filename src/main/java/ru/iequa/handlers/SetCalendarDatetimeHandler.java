package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.SetCalendarDatetimeRequest;
import ru.iequa.database.DB;
import ru.iequa.httpserver.ClientsStorage;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class SetCalendarDatetimeHandler extends HandlerBase {

    public static final String PATH = "set-service-provision-date";

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
        String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        SetCalendarDatetimeRequest request = JsonWorker.getInstance().deserialize(json, SetCalendarDatetimeRequest.class);
        final var token = exchange.getRequestHeaders().get("Token");
        final var sdt = request.selectedDatetime;
        final var hours = Integer.parseInt(sdt.time.substring(0, 2));//08:03
        final var minutes = Integer.parseInt(sdt.time.substring(3, 5));
        final var ldt = LocalDateTime.parse("yyyy-mm-ddT00:00"
                        .replace("yyyy", sdt.year)
                        .replace("mm", sdt.month.length() > 1 ? sdt.month : "0" + sdt.month)
                        .replace("dd", sdt.day.length() > 1 ? sdt.day : "0" + sdt.day)
                ).withHour(hours)
                .withMinute(minutes)
                .withSecond(0);
        if (token != null && ClientsStorage.isClientUUIDExists(UUID.fromString(token.get(0)))) {

            final var sql = "insert into public.user_services (user_id, service_id, provision_date) values (:first, :second, ':third')"
                    .replace(":first", ClientsStorage.getClientId(UUID.fromString(token.get(0))))
                    .replace(":second", "1")
                    .replace(":third", Timestamp.valueOf(ldt).toString());
            final var res = DB.getInstance().ExecInsert(sql);
            if (res == 1) {
                new ResponseCreator().sendOkResponseWithMessage(exchange, "Запись успешна!");
                return;
            }
        }
        new ResponseCreator().sendOkResponseWithMessage(exchange, "it worked");
    }
}
