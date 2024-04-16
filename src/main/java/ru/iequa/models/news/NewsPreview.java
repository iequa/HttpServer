package ru.iequa.models.news;

public class NewsPreview {
    final int id;
    final String shortTitle;
    final String previewImage;

    public NewsPreview(int id, String shortTitle, String previewImage) {
        this.id = id;
        this.shortTitle = shortTitle;
        this.previewImage = previewImage;
    }
}
