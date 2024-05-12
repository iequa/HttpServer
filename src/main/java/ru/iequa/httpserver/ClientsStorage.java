package ru.iequa.httpserver;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public class ClientsStorage {
    private static final HashMap<UUID, ClientInfo> clients = new HashMap<>();

    public static void checkDates(LocalDateTime ldt) {
        clients.forEach(
                (key, val) -> {
                    if (val.expireDate.isAfter(ldt)) {
                        clients.remove(key);
                    }
                });
    }

    public static boolean contains(ClientInfo ci) {
        return clients.containsValue(ci);
    }

    public static boolean isClientExists(String id, UUID uuid) {
        return isClientIdExists(id) && clients.containsKey(uuid);
    }

    public static boolean isClientUUIDExists(UUID uuid) {
        return clients.containsKey(uuid);
    }

    public static boolean isClientIdExists(String id) {
        return clients.values().stream().anyMatch(val -> val.id.equals(id));
    }

    public static String getClientIp(UUID id) {
        return clients.get(id).ip;
    }

    public static String getClientId(UUID uuid) {
        return clients.get(uuid).id;
    }

    public static void addClient(String id, UUID uuid, String ip, LocalDateTime ldt, boolean isAdmin) {
        var clientInfo = new ClientInfo(id, ip, ldt, isAdmin);
        clients.put(uuid, clientInfo);
    }

    public static void deleteClient(UUID uuid) {
        clients.remove(uuid);
    }
}
