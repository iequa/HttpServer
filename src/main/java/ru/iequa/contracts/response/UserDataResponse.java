package ru.iequa.contracts.response;

import ru.iequa.contracts.response.base.BaseResponse;
import ru.iequa.contracts.response.base.ResponseBase;
import ru.iequa.models.serviceinfo.ServiceInfo;

import java.util.List;

public class UserDataResponse extends BaseResponse implements ResponseBase {
    public final String login;
    public final String surname;
    public final String name;
    public final String gender;
    public final List<ServiceInfo> serviceInfos;

    public UserDataResponse(String errorMessage, int code, String login, String surname, String name, String gender, List<ServiceInfo> serviceInfos) {
        super(errorMessage, code);
        this.login = login;
        this.surname = surname;
        this.name = name;
        this.gender = gender;
        this.serviceInfos = serviceInfos;
    }

    public UserDataResponse(String login, String surname, String name, String gender, List<ServiceInfo> serviceInfos) {
        this(null, 200, login, surname, name, gender, serviceInfos);
    }
}
