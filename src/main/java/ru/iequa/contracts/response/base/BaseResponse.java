package ru.iequa.contracts.response.base;

public class BaseResponse extends Response {
    public BaseResponse(String errorMessage, int code) {
        super(errorMessage, code);
    }

    @Override
    public int getCode() {
        return code;
    }
}
