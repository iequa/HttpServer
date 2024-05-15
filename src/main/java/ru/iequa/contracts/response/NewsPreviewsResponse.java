package ru.iequa.contracts.response;

import ru.iequa.contracts.response.base.BaseResponse;
import ru.iequa.models.news.NewsPreviewData;

import java.util.List;

public class NewsPreviewsResponse extends BaseResponse {
    public final List<NewsPreviewData> newsPreviewData;
    public final int page;
    public final int pagesCount;

    public NewsPreviewsResponse(String errorMessage, int code, List<NewsPreviewData> newsPreviewData, int page, int pagesCount) {
        super(errorMessage, code);
        this.newsPreviewData = newsPreviewData;
        this.page = page;
        this.pagesCount = pagesCount;
    }

    public NewsPreviewsResponse(List<NewsPreviewData> newsPreviewData, int page, int pagesCount) {
        this(null, 200, newsPreviewData, page, pagesCount);
    }
}

