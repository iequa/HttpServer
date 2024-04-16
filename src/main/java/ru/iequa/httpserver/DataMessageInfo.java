package ru.iequa.httpserver;

import java.time.LocalDateTime;

public class DataMessageInfo {
    public final LocalDateTime date;
    public final String message;
    public boolean sent;

    public DataMessageInfo(LocalDateTime date, String message) {
        this.date = date;
        this.message = message;
        sent = false;
    }
}
