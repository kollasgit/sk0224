package com.toolrental.utils;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
public class FormatUtilities {
    public static final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
    public static final NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.US);
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");

    public static String printAmount(final double amt) {
        return numberFormat.format(amt);
    }
    public static String printPercent(final double in) {
        percentFormat.setMaximumFractionDigits(0);
        return percentFormat.format(in/100);
    }
    public static String printDate(final LocalDate ld) {
        return ld.format(dateFormatter);
    }
}