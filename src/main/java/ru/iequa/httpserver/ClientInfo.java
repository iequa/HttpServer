package ru.iequa.httpserver;

import java.time.LocalDateTime;

public class ClientInfo {
    public String id;
    public String ip;
    public final LocalDateTime expireDate;
    public boolean isAdmin;

    public ClientInfo(String id, String ip, LocalDateTime expireDate, boolean isAdmin) {
        this.id = id;
        this.expireDate = expireDate;
        this.ip = ip;
        this.isAdmin = isAdmin;
    }
}
