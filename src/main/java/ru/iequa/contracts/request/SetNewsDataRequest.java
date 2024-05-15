package ru.iequa.contracts.request;

import ru.iequa.models.news.NewsData;

public class SetNewsDataRequest {
    public final NewsData data;

    public SetNewsDataRequest(NewsData data) {
        this.data = data;
    }
}
