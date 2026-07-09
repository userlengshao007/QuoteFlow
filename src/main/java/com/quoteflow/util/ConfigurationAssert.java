package com.quoteflow.util;

import com.quoteflow.exception.BusinessException;
import com.quoteflow.exception.ErrorCodeEnum;
import org.springframework.util.StringUtils;

/**
 * 配置校验工具。
 *
 * @author QuoteFlow
 */
public final class ConfigurationAssert {

    private ConfigurationAssert() {
    }

    /**
     * 要求配置值不能为空。
     *
     * @param value 配置值
     * @param name 配置名称
     * @param <T> 值类型
     * @return 原始值
     */
    public static <T> T requireNonNull(T value, String name) {
        if (value == null) {
            throw new BusinessException(ErrorCodeEnum.MISSING_CONFIGURATION, name + " 未配置");
        }
        return value;
    }

    /**
     * 要求字符串配置值不能为空白。
     *
     * @param value 配置值
     * @param name 配置名称
     * @return 原始值
     */
    public static String requireNonBlank(String value, String name) {
        if (!StringUtils.hasText(value)) {
            throw new BusinessException(ErrorCodeEnum.MISSING_CONFIGURATION, name + " 未配置");
        }
        return value;
    }
}
