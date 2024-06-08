package ru.iequa.contracts.request;

import ru.iequa.models.calendar.DayMouth;

import java.util.List;
import java.util.UUID;

public class CalendarDataRequest extends Request {
    public final List<DayMouth> days;
    public final String type;
    public final int typeId;

    public CalendarDataRequest(int id, UUID clientId, int page, List<DayMouth> days, String type, int typeId) {
        super(id, clientId, page);
        this.days = days;
        this.type = type;
        this.typeId = typeId;
    }
}
