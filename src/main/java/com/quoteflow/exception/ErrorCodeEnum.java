package com.quoteflow.exception;

/**
 * Business error code enum.
 *
 * @author QuoteFlow
 */
public enum ErrorCodeEnum {

    /**
     * Invalid request parameters.
     */
    INVALID_PARAMETER("400", "请求参数不合法"),

    /**
     * Required platform configuration is missing.
     */
    MISSING_CONFIGURATION("5001", "平台配置缺失"),

    /**
     * Chaoxing SDK call failed.
     */
    CHAOXING_SDK_ERROR("5002", "超星接口调用失败"),

    /**
     * Customer data does not exist.
     */
    CUSTOMER_NOT_FOUND("6001", "客户信息不存在"),

    /**
     * Project data does not exist.
     */
    PROJECT_NOT_FOUND("6002", "项目信息不存在");

    /**
     * Error code.
     */
    private final String code;

    /**
     * Error message.
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
