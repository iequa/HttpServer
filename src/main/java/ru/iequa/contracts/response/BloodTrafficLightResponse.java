package ru.iequa.contracts.response;

import ru.iequa.contracts.response.base.BaseResponse;
import ru.iequa.models.blooddata.BloodData;

import java.util.List;

public class BloodTrafficLightResponse extends BaseResponse {
    public final String uuid;
    public final List<BloodData> bloodData;


    public BloodTrafficLightResponse(String uuid, String errorMessage, int code, List<BloodData> bloodData) {
        super(errorMessage, code);
        this.uuid = uuid;
        this.bloodData = bloodData;
    }
}

