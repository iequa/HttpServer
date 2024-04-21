package ru.iequa.models.news;

import java.sql.Timestamp;

public class NewsPreview {
    final int id;
    final String shortTitle;
    final String shortBody;
    final String previewImage;

    final Timestamp date;

    public NewsPreview(int id, String shortTitle, String shortBody, String previewImage, Timestamp date) {
        this.id = id;
        this.shortTitle = shortTitle;
        this.shortBody = shortBody;
        this.previewImage = previewImage;
        this.date = date;
    }
}
