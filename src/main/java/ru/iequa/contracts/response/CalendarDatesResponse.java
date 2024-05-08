package ru.iequa.contracts.response;

import ru.iequa.contracts.response.base.BaseResponse;

import java.util.List;

public class CalendarDatesResponse extends BaseResponse {
    public final List<String> dates;
    public final String type;

    public CalendarDatesResponse(String errorMessage, int code, List<String> dates, String type) {
        super(errorMessage, code);
        this.dates = dates;
        this.type = type;
    }

    public CalendarDatesResponse(List<String> dates, String type) {
        this(null, 200, dates, type);
    }
}

