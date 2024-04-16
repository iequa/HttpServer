package ru.iequa.models.blooddata;

import java.util.List;

public class BloodData {
    public final String name;
    public final List<RHData> rhs;

    public BloodData(String name, List<RHData> rhs) {
        this.name = name;
        this.rhs = rhs;
    }
}
