package com.vaadin.starter.bakery.ui.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;

/**
 * Utility class for formatting various data types including dates, times, and currency values.
 * Provides standardized formatting methods used throughout the application UI.
 */
public class FormattingUtils {

    /** Decimal format pattern for zero values. */
    public static final String DECIMAL_ZERO = "0.00";

    /**
     * Date formatter for month abbreviation and day number. E.g: Nov 20
     */
    public static final DateTimeFormatter MONTH_AND_DAY_FORMATTER = DateTimeFormatter.ofPattern("MMM d",
            BakeryConst.APP_LOCALE);

    /**
     * Date formatter for full day name. E.g: Monday
     */
    public static final DateTimeFormatter WEEKDAY_FULLNAME_FORMATTER = DateTimeFormatter.ofPattern("EEEE",
            BakeryConst.APP_LOCALE);

    /**
     * Temporal field for getting the week of the year from local date.
     */
    public static final TemporalField WEEK_OF_YEAR_FIELD = WeekFields.of(BakeryConst.APP_LOCALE).weekOfWeekBasedYear();

    /**
     * Date formatter for abbreviated day of week and day number. E.g: Mon 20
     */
    public static final DateTimeFormatter SHORT_DAY_FORMATTER = DateTimeFormatter.ofPattern("E d",
            BakeryConst.APP_LOCALE);

    /**
     * Date formatter for full date format. E.g: 03.03.2001
     */
    public static final DateTimeFormatter FULL_DATE_FORMATTER = DateTimeFormatter
            .ofPattern("dd.MM.yyyy", BakeryConst.APP_LOCALE);

    /**
     * Time formatter for hours with am/pm. E.g: 2:00 PM
     */
    public static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormatter
            .ofPattern("h:mm a", BakeryConst.APP_LOCALE);

    /**
     * Returns the full month name of the date according to the application locale.
     *
     * @param date the LocalDate to get the month name for
     * @return the full month name, e.g., "November"
     */
    public static String getFullMonthName(LocalDate date) {
        return date.getMonth().getDisplayName(TextStyle.FULL, BakeryConst.APP_LOCALE);
    }

    /**
     * Formats an integer value representing cents as a currency string.
     *
     * @param valueInCents the value to format (in cents)
     * @return the formatted currency string, e.g., "$10.50" for 1050 cents
     */
    public static String formatAsCurrency(int valueInCents) {
        return NumberFormat.getCurrencyInstance(BakeryConst.APP_LOCALE).format(BigDecimal.valueOf(valueInCents, 2));
    }

    /**
     * Creates a DecimalFormat instance for formatting UI prices.
     * The formatter uses the application locale and does not use grouping separators.
     *
     * @return a DecimalFormat instance configured for price formatting
     */
    public static DecimalFormat getUiPriceFormatter() {
        DecimalFormat formatter = new DecimalFormat("#" + DECIMAL_ZERO,
                DecimalFormatSymbols.getInstance(BakeryConst.APP_LOCALE));
        formatter.setGroupingUsed(false);
        return formatter;
    }
}