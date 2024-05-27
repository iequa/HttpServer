package ru.iequa.contracts.response;

import ru.iequa.contracts.response.base.BaseResponse;
import ru.iequa.contracts.response.base.ResponseBase;

public class SetNewsDataResponse extends BaseResponse implements ResponseBase {
    public final int id;

    public SetNewsDataResponse(String errorMessage, String message, int code, int id) {
        super(errorMessage, message, code);
        this.id = id;
    }
}
