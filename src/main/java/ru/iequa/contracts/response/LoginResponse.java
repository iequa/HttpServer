package ru.iequa.contracts.response;

import ru.iequa.contracts.response.base.BaseResponse;
import ru.iequa.contracts.response.base.ResponseBase;
import ru.iequa.models.IdDate;

import java.util.List;

public class LoginResponse extends BaseResponse implements ResponseBase {
    public final String token;
    public final String name;
    public final List<IdDate> nextDonationDate;
    public final boolean specialFunctions;

    public LoginResponse(String errorMessage, String message, int code, String token, String name, List<IdDate> nextDonationDates, boolean specialFunctions) {
        super(errorMessage, message, code);
        this.token = token;
        this.name = name;
        this.nextDonationDate = nextDonationDates;
        this.specialFunctions = specialFunctions;
    }
}
