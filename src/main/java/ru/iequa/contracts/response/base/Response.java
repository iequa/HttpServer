package ru.iequa.contracts.response.base;

abstract class Response implements ResponseBase {
    public final int code;
    public final String errorMessage;

    protected Response(String errorMessage, int code) {
        this.errorMessage = errorMessage;
        this.code = code;
    }
}
