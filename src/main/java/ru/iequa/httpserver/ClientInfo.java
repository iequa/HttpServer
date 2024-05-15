package ru.iequa.httpserver;

import java.time.LocalDateTime;

public class ClientInfo {
    public String id;
    public String ip;
    public final LocalDateTime expireDate;
    public boolean specialFunctions;

    public ClientInfo(String id, String ip, LocalDateTime expireDate, boolean specialFunctions) {
        this.id = id;
        this.expireDate = expireDate;
        this.ip = ip;
        this.specialFunctions = specialFunctions;
    }
}
