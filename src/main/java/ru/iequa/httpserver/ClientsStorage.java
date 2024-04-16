package ru.iequa.httpserver;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ClientsStorage {
    private static final HashMap<String, ClientInfo> clients = new HashMap<>();

    public static void init() {
        var data = getIdsFromProps();
        clients.putAll(data);
    }

    public static LocalDateTime getMaxDate() {
        final LocalDateTime[] maxDate = {LocalDateTime.MIN};
        clients.forEach(
                (key, val) -> val.dataMessageInfos.forEach(dmi -> {
                    if (dmi.date.isAfter(maxDate[0])) {
                        maxDate[0] = dmi.date;
                    }
                }));
        return maxDate[0];
    }

    public static HashMap<String, HashSet<DataMessageInfo>> checkTime(LocalDateTime ldt) {
        final HashMap<String, HashSet<DataMessageInfo>> res = new HashMap<>();
        clients.forEach(
                (key, val) -> {
                    if (val.ip != null && !val.ip.isEmpty()) {
                        HashSet<DataMessageInfo> dmii = new HashSet<>();
                        val.dataMessageInfos.forEach(dmi -> {
                            if (dmi.date.isBefore(ldt) && !dmi.sent) {
                                dmii.add(dmi);
                                dmi.sent = true;
                            }
                        });
                        if (dmii.size() > 0) {
                            res.put(key, dmii);
                        }
                    }
                });
        return res;
    }

    public static boolean isClientExists(String id, UUID uuid) {
        return isClientIdExists(id) && clients.get(id).uuid.equals(uuid);
    }

    public static boolean isClientIdExists(String id) {
        return clients.containsKey(id);
    }

    public static String getClientIp(String id) {
        return clients.get(id).ip;
    }

    public static void addClientUUID(String id, UUID uuid, String ip) {
        var clientInfo = clients.get(id);
        clientInfo.uuid = uuid;
        clientInfo.ip = ip;
        clients.put(id, clientInfo);
    }

    private static HashMap<String, ClientInfo> getIdsFromProps() {
        var currentPath = System.getProperty("user.dir")
                + "%s%s".formatted(System.getProperty("os.name").toLowerCase().contains("windows") ?
                        "\\"
                        :
                        "/"
                ,
                "config.cfg"
        );
        var propFile = new File(currentPath);
        var properties = new Properties();
        try {
            properties.load(new FileReader(propFile));
            String idsStr = (String) properties.get("targetIds");
            var ids = Arrays.stream(idsStr.split(",")).toList();
            HashMap<String, ClientInfo> res = new HashMap<>();
            for (var id : ids) {
                HashSet<DataMessageInfo> dataMessageInfos = readDataMessageInfos(properties, id);
                res.put(id, new ClientInfo(null, dataMessageInfos));
            }
            return res;
        } catch (Exception e) {
            System.out.println("Prop file not found");
        }
        return null;
    }

    private static HashSet<DataMessageInfo> readDataMessageInfos(Properties properties, String id) {
        //Формируем список из дат и сообщений
        var dates = (String) properties.get(id + "dates");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        final List<LocalDateTime> dateTimes = Arrays.stream(
                        dates.split(","))
                .map(d -> LocalDateTime.parse(d, formatter))
                .toList();

        var message = (String) properties.get(id + "messages");
        final List<String> messages = new ArrayList<>(Arrays.asList(message.split(",")));

        HashSet<DataMessageInfo> dmi = new HashSet<>();
        for (int i = 0; i < dateTimes.size(); i++) {
            dmi.add(new DataMessageInfo(dateTimes.get(i), messages.get(i)));
        }
        return dmi;
    }
}
