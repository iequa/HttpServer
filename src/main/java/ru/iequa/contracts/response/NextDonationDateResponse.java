package ru.iequa.contracts.response;

import ru.iequa.contracts.response.base.BaseResponse;

import java.sql.Timestamp;

public class NextDonationDateResponse extends BaseResponse {

    public final Timestamp nextDonationDate;

    public NextDonationDateResponse(String errorMessage, String message, int code, Timestamp nextDonationDate) {
        super(errorMessage, message, code);
        this.nextDonationDate = nextDonationDate;
    }

    public NextDonationDateResponse(String message, int code, Timestamp nextDonationDate) {
        this(null, message, code, nextDonationDate);
    }
}
