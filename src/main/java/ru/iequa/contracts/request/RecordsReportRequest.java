package ru.iequa.contracts.request;

import java.util.UUID;

public class RecordsReportRequest extends Request {
    public final String firstDate;
    public final String secondDate;

    public RecordsReportRequest(int id, UUID clientId, int page, String firstDate, String secondDate) {
        super(id, clientId, page);
        this.firstDate = firstDate;
        this.secondDate = secondDate;
    }
}
