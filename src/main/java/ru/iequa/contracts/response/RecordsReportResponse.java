package ru.iequa.contracts.response;

import ru.iequa.contracts.response.base.BaseResponse;

public class RecordsReportResponse extends BaseResponse {
    final byte[] respFile;
    final String respFileName;

    public RecordsReportResponse(byte[] respFile, String respFileName) {
        super(null, 200);
        this.respFile = respFile;
        this.respFileName = respFileName;
    }
}
