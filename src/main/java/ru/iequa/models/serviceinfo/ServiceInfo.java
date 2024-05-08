package ru.iequa.models.serviceinfo;

public class ServiceInfo {
    final public String name;
    final public String type;
    final public String cost;

    public ServiceInfo(String name, String type, String cost) {
        this.name = name;
        this.type = type;
        this.cost = cost;
    }
}
