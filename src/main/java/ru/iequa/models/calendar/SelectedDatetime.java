package ru.iequa.models.calendar;

public class SelectedDatetime {
    public final String day;
    public final String month;
    public final String year;
    public final String fullDate;
    public final String time;

    public SelectedDatetime(String day, String month, String year, String fullDate, String time) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.fullDate = fullDate;
        this.time = time;
    }
}
