package com.toolrental.holiday;

import java.time.LocalDate;
import java.time.MonthDay;

public interface IHolidayCalendar {

    public void addHoliday(final MonthDay monthDay, final int year);

    public boolean isHoliday(LocalDate date);

    public boolean isWeekend(LocalDate date);
}
