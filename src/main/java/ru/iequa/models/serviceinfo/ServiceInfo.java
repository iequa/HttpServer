package ru.iequa.models.serviceinfo;

public class ServiceInfo {
    final public String name;
    final public String type;
    final public String cost;
    final public String provision_date;

    public ServiceInfo(String name, String type, String cost, String provisionDate) {
        this.name = name;
        this.type = type;
        this.cost = cost;
        provision_date = provisionDate;
    }
}
