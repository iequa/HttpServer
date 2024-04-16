package ru.iequa.contracts;

import java.util.UUID;

public class Request {
    public final int id;
    public final UUID clientId;
    public final int page;

    public Request(int id, UUID clientId, int page) {
        this.id = id;
        this.clientId = clientId;
        this.page = page;
    }
}
