package com.toolrental.model;

public class ToolType {

    String id;

    String type;

    double dailyCharge;

    boolean weekdayCharge;

    boolean weekendCharge;

    boolean HolidayCharge;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getDailyCharge() {
        return dailyCharge;
    }

    public boolean isWeekdayCharge() {
        return weekdayCharge;
    }

    public boolean isWeekendCharge() {
        return weekendCharge;
    }

    public boolean isHolidayCharge() {
        return HolidayCharge;
    }

    public ToolType(String id, String type, double dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
        this.id = id;
        this.type = type;
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        HolidayCharge = holidayCharge;
    }
}
