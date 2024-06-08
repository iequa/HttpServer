package ru.iequa.models.serviceinfo;

public class ServiceInfo {
    final public int id;
    final public String name;
    final public String type;
    final public String cost;
    final public String provision_date;

    public ServiceInfo(int id, String name, String type, String cost, String provisionDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.cost = cost;
        provision_date = provisionDate;
    }
}
