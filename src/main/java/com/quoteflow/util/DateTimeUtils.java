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
 * 日期时间工具。
 *
 * @author zhangyujie
 */
public final class DateTimeUtils {

    /**
     * 日期格式化器。
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateTimeUtils() {
    }

    /**
     * 将 yyyy-MM-dd 日期转换为当天开始的毫秒时间戳。
     *
     * @param date 日期字符串
     * @return 毫秒时间戳
     */
    public static Long toStartOfDayMillis(String date) {
        return toMillis(date, LocalTime.MIN);
    }

    /**
     * 将 yyyy-MM-dd 日期转换为当天结束的毫秒时间戳。
     *
     * @param date 日期字符串
     * @return 毫秒时间戳
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
