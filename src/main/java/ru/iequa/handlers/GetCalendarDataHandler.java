package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.CalendarDataRequest;
import ru.iequa.contracts.response.CalendarDatesResponse;
import ru.iequa.database.DB;
import ru.iequa.models.db.DBResult;
import ru.iequa.models.db.Row;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class GetCalendarDataHandler extends HandlerBase {

    public static final String PATH = "get-calendar-data";

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
        final String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        final CalendarDataRequest request = JsonWorker.getInstance().deserialize(json, CalendarDataRequest.class);
        final String currYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        final var selectionDates = new ArrayList<String>();
        request.days.forEach(day -> selectionDates.add("':date'"
                .replace(":date",
                        LocalDate.parse(
                                currYear + "-"
                                        + (day.month.length() > 1 ? day.month : "0" + day.month) + "-"
                                        + (day.day.length() > 1 ? day.day : "0" + day.day),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        ).toString()
                )
        ));
        DBResult res = DB.getInstance().ExecQuery("select us.provision_date from public.user_services us where date(us.provision_date) in (" + String.join(",", selectionDates) + ")");
        final var rows = res.getRows();
        if (!rows.isEmpty()) {
            final var dates = new ArrayList<String>();
            for (Row row : rows) {
                final Timestamp date = Timestamp.valueOf(row.getElement("provision_date").toString());
                dates.add(date.toString());
            }
            new ResponseCreator().sendResponseWithBody(
                    exchange,
                    new CalendarDatesResponse(
                            dates
                    )
            );
            return;
        }
        new ResponseCreator().sendResponseWithCode(exchange, 400, "Записей нет!");
    }
}
