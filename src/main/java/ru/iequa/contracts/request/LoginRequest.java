package ru.iequa.contracts.request;

import java.util.UUID;

public class LoginRequest extends Request {
    public final String login;
    public final String value;

    public LoginRequest(int id, UUID clientId, int page, String login, String value) {
        super(id, clientId, page);
        this.login = login;
        this.value = value;
    }
}
