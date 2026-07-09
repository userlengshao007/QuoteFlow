package com.quoteflow.exception;

/**
 * 业务错误码枚举。
 *
 * @author QuoteFlow
 */
public enum ErrorCodeEnum {

    /**
     * 请求参数不合法。
     */
    INVALID_PARAMETER("400", "请求参数不合法"),

    /**
     * 必要平台配置缺失。
     */
    MISSING_CONFIGURATION("5001", "平台配置缺失"),

    /**
     * 超星 SDK 调用失败。
     */
    CHAOXING_SDK_ERROR("5002", "超星接口调用失败"),

    /**
     * 客户数据不存在。
     */
    CUSTOMER_NOT_FOUND("6001", "客户信息不存在"),

    /**
     * 项目数据不存在。
     */
    PROJECT_NOT_FOUND("6002", "项目信息不存在");

    /**
     * 错误码。
     */
    private final String code;

    /**
     * 错误消息。
     */
    private final String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
