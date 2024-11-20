package utility;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Utility class for formatting dates and times in various formats.
 */
public class DateFormat {

    // Define the common date formatter for dd-MM-yyyy format
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static final SimpleDateFormat LEGACY_DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat LEGACY_DATE_TIME_FORMATTER = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    /**
     * Formats a {@link LocalDate} to the "dd-MM-yyyy" format.
     *
     * @param date The {@link LocalDate} to format.
     * @return A formatted date string, or an empty string if the input is null.
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMATTER.format(date);
    }

    /**
     * Formats a {@link LocalDateTime} to the "dd-MM-yyyy HH:mm" format.
     *
     * @param dateTime The {@link LocalDateTime} to format.
     * @return A formatted date-time string, or an empty string if the input is null.
     */
    public static String formatWithTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return DATE_TIME_FORMATTER.format(dateTime);
    }

    /**
     * Formats a {@link Date} (legacy java.util.Date) to the "dd-MM-yyyy" format.
     *
     * @param date The {@link Date} to format.
     * @return A formatted date string, or an empty string if the input is null.
     */
    public static String format(Date date) {
        if (date == null) {
            return "";
        }
        return LEGACY_DATE_FORMATTER.format(date);
    }

    /**
     * Formats a {@link Date} (legacy java.util.Date) to the "dd-MM-yyyy HH:mm" format.
     *
     * @param date The {@link Date} to format.
     * @return A formatted date-time string, or an empty string if the input is null.
     */
    public static String formatWithTime(Date date) {
        if (date == null) {
            return "";
        }
        return LEGACY_DATE_TIME_FORMATTER.format(date);
    }

    /**
     * Formats a {@link LocalDateTime} or {@link LocalDate} to the "dd-MM-yyyy" format.
     * If a {@link LocalDateTime} is passed, it extracts and formats the date part.
     *
     * @param date The date or date-time object to format.
     * @return A formatted date string, or an empty string if the input is null.
     * @throws IllegalArgumentException If the input type is unsupported.
     */
    public static String formatNoTime(Object date) {
        if (date == null) {
            return "";
        }
        if (date instanceof LocalDate) {
            return format((LocalDate) date);
        }
        if (date instanceof LocalDateTime) {
            return format(((LocalDateTime) date).toLocalDate());
        }
        throw new IllegalArgumentException("Unsupported date type: " + date.getClass().getName());
    }

    /**
     * Parses a date string and formats it to the "dd-MM-yyyy" format.
     * Accepts inputs in "d-M-yyyy" or "dd-MM-yyyy" formats.
     *
     * @param dateStr The input date string to parse and format.
     * @return A formatted date string, or the original input if parsing fails.
     */
    public static String format(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "";
        }

        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("[d-M-yyyy][dd-MM-yyyy]"));
            return DATE_FORMATTER.format(date);
        } catch (Exception e) {
            System.err.println("Invalid date format: " + dateStr);
            return dateStr;
        }
    }

    /**
     * Parses a date-time string and formats it to the "dd-MM-yyyy HH:mm" format.
     * Accepts inputs in "d-M-yyyy HH:mm" or "dd-MM-yyyy HH:mm" formats.
     *
     * @param dateStr The input date-time string to parse and format.
     * @return A formatted date-time string, or the original input if parsing fails.
     */
    public static String formatWithTime(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "";
        }

        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("[d-M-yyyy HH:mm][dd-MM-yyyy HH:mm]"));
            return DATE_TIME_FORMATTER.format(dateTime);
        } catch (Exception e) {
            System.err.println("Invalid date-time format: " + dateStr);
            return dateStr;
        }
    }

    /**
     * Formats an object representing a date or date-time to an appropriate format.
     * Supports {@link LocalDate}, {@link LocalDateTime}, and {@link Date}.
     *
     * @param date The date object to format.
     * @return A formatted date or date-time string, or an empty string if the input is null.
     * @throws IllegalArgumentException If the input type is unsupported.
     */
    public static String format(Object date) {
        if (date == null) {
            return "";
        }
        if (date instanceof LocalDate) {
            return format((LocalDate) date);
        }
        if (date instanceof LocalDateTime) {
            return formatWithTime((LocalDateTime) date);
        }
        if (date instanceof Date) {
            return formatWithTime((Date) date);
        }
        throw new IllegalArgumentException("Unsupported date type: " + date.getClass().getName());
    }
}
