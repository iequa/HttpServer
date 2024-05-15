package ru.iequa.models.news;

import java.sql.Timestamp;

public class NewsData extends NewsPreviewData {
    public final String title;
    public final String body;

    public NewsData(int id, String shortTitle, String shortBody, String previewImage, Timestamp date, String title, String body) {
        super(id, shortTitle, shortBody, previewImage, date);
        this.title = title;
        this.body = body;
    }
}
