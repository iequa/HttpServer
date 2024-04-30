package ru.iequa.contracts.response;

import ru.iequa.contracts.response.base.BaseResponse;

import java.util.List;

public class CalendarDatesResponse extends BaseResponse {
    public final List<String> dates;

    public CalendarDatesResponse(String errorMessage, int code, List<String> dates) {
        super(errorMessage, code);
        this.dates = dates;
    }

    public CalendarDatesResponse(List<String> dates) {
        this(null, 200, dates);
    }
}

