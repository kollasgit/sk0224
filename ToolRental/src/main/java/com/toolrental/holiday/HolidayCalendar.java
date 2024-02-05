package com.toolrental.holiday;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

//Manages the holidays, automatically loads/sets the holidays for this year and next two years
//Ideally, these holidays should be stored in DB
//An api can be created for addHoliday so the admin can add the holidays for future years
public class HolidayCalendar implements IHolidayCalendar{
    private HashMap<String, Set<MonthDay>> yearHolidays;
    private Set<LocalDate> holidays;
    private static HolidayCalendar instance;
    private static Logger LOGGER = Logger.getLogger("Holiday");


    // Static method to return the singleton instance
    public static HolidayCalendar getInstance() {
        if (instance == null) {
            synchronized (HolidayCalendar.class) {
                if (instance == null) {
                    instance = new HolidayCalendar();
                }
            }
        }
        return instance;
    }

    //To begin with, initialize for this year and next two years
    public HolidayCalendar() {
        if (this.yearHolidays == null || this.yearHolidays.isEmpty()) {
            this.yearHolidays = new HashMap<>();
        }
        int thisYear = LocalDate.now().getYear();
        initializeHolidays(thisYear);
        initializeHolidays(thisYear+1);
        initializeHolidays(thisYear+2);
    }

    // Add adjusted holidays to the set
    private void initializeHolidays(int year) {
        String methodName = "initializeHolidays for year " + year;
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Start " + methodName);
        }
        //Set<MonthDay>holidays = new HashSet<MonthDay>();
        LocalDate indDay = LocalDate.of(year, Month.JULY, 4);
        // Independence Day - adjusts it to the closes weekday
        if (indDay.getDayOfWeek() == DayOfWeek.SATURDAY)
            addHoliday(MonthDay.of(Month.JULY, 3), year);
        else if (indDay.getDayOfWeek() == DayOfWeek.SUNDAY)
            addHoliday(MonthDay.of(Month.JULY, 5), year);
        else
            addHoliday(MonthDay.of(Month.JULY, 4), year);

        // Labor Day - adjusts it to the first monday of September
        LocalDate laborDay = LocalDate.of(year, Month.SEPTEMBER, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        addHoliday(MonthDay.of(Month.SEPTEMBER, laborDay.getDayOfMonth()), year);

        //this.yearHolidays.put(String.valueOf(year), holidays);
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("End " + methodName);
        }
    }

    public void addHoliday(final MonthDay monthDay, final int year) {
        String methodName = "addHoliday " + monthDay + " " + year;
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Start " + methodName);
        }
        Set<MonthDay> holidays = this.yearHolidays.get(String.valueOf(year));
        if(holidays == null) {
            holidays = new HashSet<MonthDay>();
        }
        holidays.add(monthDay);
        this.yearHolidays.put(String.valueOf(year), holidays);
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("End " + methodName);
        }
    }

    public boolean isHoliday(final LocalDate date) {
        String year = String.valueOf(date.getYear());
        if (this.yearHolidays == null || this.yearHolidays.isEmpty()) {
            this.initializeHolidays(date.getYear());
        }
        if (this.yearHolidays.get(year) == null || this.yearHolidays.get(year).isEmpty()) {
            this.initializeHolidays(date.getYear());
        }
        return this.yearHolidays.get(year).contains(toMonthDay(date));
    }

    private static MonthDay toMonthDay(final LocalDate localDate) {
        return MonthDay.of(localDate.getMonth(), localDate.getDayOfMonth());
    }

    public boolean isWeekend(final LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}