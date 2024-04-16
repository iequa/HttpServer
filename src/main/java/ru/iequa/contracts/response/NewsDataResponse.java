package ru.iequa.contracts.response;

import ru.iequa.contracts.response.base.BaseResponse;
import ru.iequa.contracts.response.base.ResponseBase;

import java.sql.Timestamp;

public class NewsDataResponse extends BaseResponse implements ResponseBase {
    public final int id;
    public final String title;
    public final String body;
    public final Timestamp date;
    public final String img;

    public NewsDataResponse(String errorMessage, int code, int id, String title, String body, Timestamp date, String img) {
        super(errorMessage, code);
        this.id = id;
        this.title = title;
        this.body = body;
        this.date = date;
        this.img = img;
    }

    public NewsDataResponse(int id, String title, String body, Timestamp date, String img) {
        this("", 200, id, title, body, date, img);
    }
}
