package com.quoteflow.util;

import com.quoteflow.exception.BusinessException;
import com.quoteflow.exception.ErrorCodeEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Date time utility.
 *
 * @author QuoteFlow
 */
public final class DateTimeUtils {

    /**
     * Date formatter.
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateTimeUtils() {
    }

    /**
     * Converts yyyy-MM-dd date to start-of-day epoch milliseconds.
     *
     * @param date date string
     * @return epoch milliseconds
     */
    public static Long toStartOfDayMillis(String date) {
        return toMillis(date, LocalTime.MIN);
    }

    /**
     * Converts yyyy-MM-dd date to end-of-day epoch milliseconds.
     *
     * @param date date string
     * @return epoch milliseconds
     */
    public static Long toEndOfDayMillis(String date) {
        return toMillis(date, LocalTime.MAX);
    }

    private static Long toMillis(String date, LocalTime localTime) {
        try {
            LocalDate localDate = LocalDate.parse(date, DATE_FORMATTER);
            LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
            return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } catch (DateTimeParseException exception) {
            throw new BusinessException(ErrorCodeEnum.INVALID_PARAMETER, "日期格式必须为 yyyy-MM-dd", exception);
        }
    }
}
