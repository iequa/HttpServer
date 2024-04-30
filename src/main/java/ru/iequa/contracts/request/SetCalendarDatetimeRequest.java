package ru.iequa.contracts.request;

import ru.iequa.models.calendar.SelectedDatetime;

import java.util.UUID;

public class SetCalendarDatetimeRequest extends Request {
    public final SelectedDatetime selectedDatetime;

    public SetCalendarDatetimeRequest(int id, UUID clientId, int page, SelectedDatetime selectedDatetime) {
        super(id, clientId, page);
        this.selectedDatetime = selectedDatetime;
    }
}
