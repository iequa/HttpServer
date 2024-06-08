package ru.iequa.contracts.request;

import ru.iequa.models.calendar.SelectedDatetime;

import java.util.UUID;

public class SetCalendarDatetimeRequest extends Request {
    public final SelectedDatetime selectedDatetime;
    public final int serviceType;

    public SetCalendarDatetimeRequest(int id, UUID clientId, int page, SelectedDatetime selectedDatetime, int serviceType) {
        super(id, clientId, page);
        this.selectedDatetime = selectedDatetime;
        this.serviceType = serviceType;
    }
}
