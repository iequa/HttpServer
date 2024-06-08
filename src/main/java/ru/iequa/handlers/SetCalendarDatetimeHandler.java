package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.SetCalendarDatetimeRequest;
import ru.iequa.contracts.response.NextDonationDateResponse;
import ru.iequa.database.DB;
import ru.iequa.httpserver.ClientsStorage;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;
import ru.iequa.utils.TokenHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
        final var token = TokenHelper.checkAndGetValidUserToken(exchange);
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
        final var nextDate = calcNextDonationDate(sdt.donationType, ldt);
        final var sql = "insert into public.user_services (user_id, service_id, provision_date, next_provision_date) values (:first, :second, ':third', ':four')"
                .replace(":first", ClientsStorage.getClientId(token))
                .replace(":second", String.valueOf(request.serviceType))
                .replace(":third", Timestamp.valueOf(ldt).toString())
                .replace(":four", nextDate.toString());
        final var res = DB.getInstance().ExecInsertOrUpdate(sql);
        if (res == 1) {
            new ResponseCreator().sendResponseWithBody(exchange, new NextDonationDateResponse("Запись успешна!", 200, nextDate));
            return;
        }
        new ResponseCreator().sendNotAuthorizedResponse(exchange, "Ошибка. Попробуйте авторизироваться повторно");
    }


    private Timestamp calcNextDonationDate(String donationType, LocalDateTime recordTime) {
        switch (donationType) {
            case "0", "1", "2", "3", "4" -> { //Цельная кровь
                return Timestamp.valueOf(recordTime.plusMonths(2).withHour(0).withMinute(0));
            }
            case "5" -> { //Плазма
                return Timestamp.valueOf(recordTime.plusWeeks(2).withHour(0).withMinute(0));
            }
            default -> {
                return Timestamp.valueOf(recordTime.plusDays(1).withHour(0).withMinute(0));
            }
        }
    }
}
