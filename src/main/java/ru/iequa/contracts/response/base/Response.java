package ru.iequa.contracts.response.base;

abstract class Response implements ResponseBase {
    public final int code;
    public final String errorMessage;
    public final String message;

    protected Response(String errorMessage, String message, int code) {
        this.errorMessage = errorMessage;
        this.message = message;
        this.code = code;
    }

    protected Response(String errorMessage, int code) {
        this(errorMessage, null, code);
    }
}
