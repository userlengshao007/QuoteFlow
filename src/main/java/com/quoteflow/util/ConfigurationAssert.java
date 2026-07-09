package com.quoteflow.util;

import com.quoteflow.exception.BusinessException;
import com.quoteflow.exception.ErrorCodeEnum;
import org.springframework.util.StringUtils;

/**
 * Configuration validation utility.
 *
 * @author QuoteFlow
 */
public final class ConfigurationAssert {

    private ConfigurationAssert() {
    }

    /**
     * Requires a non-null value.
     *
     * @param value configuration value
     * @param name configuration name
     * @param <T> value type
     * @return original value
     */
    public static <T> T requireNonNull(T value, String name) {
        if (value == null) {
            throw new BusinessException(ErrorCodeEnum.MISSING_CONFIGURATION, name + " 未配置");
        }
        return value;
    }

    /**
     * Requires a non-blank string value.
     *
     * @param value configuration value
     * @param name configuration name
     * @return original value
     */
    public static String requireNonBlank(String value, String name) {
        if (!StringUtils.hasText(value)) {
            throw new BusinessException(ErrorCodeEnum.MISSING_CONFIGURATION, name + " 未配置");
        }
        return value;
    }
}
