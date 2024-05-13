package ru.iequa.contracts.response;

import ru.iequa.contracts.response.base.BaseResponse;
import ru.iequa.contracts.response.base.ResponseBase;

public class LoginResponse extends BaseResponse implements ResponseBase {
    public final String token;
    public final String name;
    public final String gender;
    public final String nextDonationDate;

    public LoginResponse(String errorMessage, String message, int code, String token, String name, String gender, String nextDonationDate) {
        super(errorMessage, message, code);
        this.token = token;
        this.name = name;
        this.gender = gender;
        this.nextDonationDate = nextDonationDate;
    }
}
