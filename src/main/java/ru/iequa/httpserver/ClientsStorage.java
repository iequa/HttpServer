package ru.iequa.httpserver;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class ClientsStorage {
    private static HashMap<UUID, HashSet<Date>> clients = new HashMap<>();

    public static boolean IsClientExists(UUID uuid) {
        return clients.containsKey(uuid);
    }

    public static void AddClient(UUID uuid) {
        clients.put(uuid, new HashSet<>());
    }

    public static void SetClientTimer(UUID key, Date date) {
        clients.get(key).add(date);
    }
}
