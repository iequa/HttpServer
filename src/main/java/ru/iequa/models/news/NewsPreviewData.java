package ru.iequa.models.news;

import java.sql.Timestamp;

public class NewsPreviewData {
    public final int id;
    public final String shortTitle;
    public final String shortBody;
    public final String image;
    public final Timestamp date;

    public NewsPreviewData(int id, String shortTitle, String shortBody, String image, Timestamp date) {
        this.id = id;
        this.shortTitle = shortTitle;
        this.shortBody = shortBody;
        this.image = image;
        this.date = date;
    }
}
