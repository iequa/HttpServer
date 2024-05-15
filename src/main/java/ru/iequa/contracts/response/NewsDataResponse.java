package ru.iequa.contracts.response;

import ru.iequa.contracts.response.base.BaseResponse;
import ru.iequa.contracts.response.base.ResponseBase;

import java.sql.Timestamp;

public class NewsDataResponse extends BaseResponse implements ResponseBase {
    public final int id;
    public final String title;
    public final String body;
    public final Timestamp date;
    public final String image;
    public final String shortTitle;
    public final String shortBody;

    public NewsDataResponse(String errorMessage, int code, int id, String title, String body, Timestamp date, String image, String shortTitle, String shortBody) {
        super(errorMessage, code);
        this.id = id;
        this.title = title;
        this.body = body;
        this.date = date;
        this.image = image;
        this.shortTitle = shortTitle;
        this.shortBody = shortBody;
    }

    public NewsDataResponse(int id, String title, String body, Timestamp date, String image, String shortBody, String shortTitle) {
        this("", 200, id, title, body, date, image, shortTitle, shortBody);
    }
}
