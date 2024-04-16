package ru.iequa.contracts.response;

import ru.iequa.contracts.response.base.BaseResponse;
import ru.iequa.models.news.NewsPreview;

import java.util.List;

public class NewsPreviewsResponse extends BaseResponse {
    public final List<NewsPreview> newsPreviews;

    public NewsPreviewsResponse(String errorMessage, int code, List<NewsPreview> newsPreviews) {
        super(errorMessage, code);
        this.newsPreviews = newsPreviews;
    }

    public NewsPreviewsResponse(List<NewsPreview> newsPreviews) {
        this(null, 200, newsPreviews);
    }
}

