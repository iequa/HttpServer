package ru.iequa.contracts;

import java.util.UUID;

public class Request {
    public final UUID clientId;

    public Request(UUID clientId) {
        this.clientId = clientId;
    }
}
