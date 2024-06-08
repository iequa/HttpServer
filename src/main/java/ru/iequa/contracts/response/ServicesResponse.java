package ru.iequa.contracts.response;

import ru.iequa.contracts.response.base.BaseResponse;
import ru.iequa.contracts.response.base.ResponseBase;
import ru.iequa.models.serviceinfo.ServiceInfo;

import java.util.List;

public class ServicesResponse extends BaseResponse implements ResponseBase {
    public final List<ServiceInfo> serviceInfos;

    public ServicesResponse(String errorMessage, int code, List<ServiceInfo> serviceInfos) {
        super(errorMessage, code);
        this.serviceInfos = serviceInfos;
    }

    public ServicesResponse(List<ServiceInfo> serviceInfos) {
        this(null, 200, serviceInfos);
    }
}
