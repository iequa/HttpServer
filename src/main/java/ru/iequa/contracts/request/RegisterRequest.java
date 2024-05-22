package ru.iequa.contracts.request;

import java.util.UUID;

public class RegisterRequest extends Request {
    public final String login;
    public final String value;
    public final String name;
    public final String surname;
    public final String date;

    public RegisterRequest(int id, UUID clientId, int page, String login, String value, String name, String surname, String date) {
        super(id, clientId, page);
        this.login = login;
        this.value = value;
        this.name = name;
        this.surname = surname;
        this.date = date;
    }
}
