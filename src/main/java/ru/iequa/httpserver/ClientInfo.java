package ru.iequa.httpserver;

import java.util.HashSet;
import java.util.UUID;

public class ClientInfo {
    public UUID uuid;
    public String ip;
    public final HashSet<DataMessageInfo> dataMessageInfos;

    public ClientInfo(UUID uuid, HashSet<DataMessageInfo> dataMessageInfos) {
        this.uuid = uuid;
        this.dataMessageInfos = dataMessageInfos;
    }
}
