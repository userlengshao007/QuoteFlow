package com.quoteflow.dto;

/**
 * Unified API response.
 *
 * @param <T> response data type
 * @author QuoteFlow
 */
public class ApiResponse<T> {

    /**
     * Response code.
     */
    private String code;

    /**
     * Whether request succeeded.
     */
    private Boolean success;

    /**
     * Response message.
     */
    private String message;

    /**
     * Response data.
     */
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode("200");
        response.setSuccess(Boolean.TRUE);
        response.setMessage("请求成功");
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setSuccess(Boolean.FALSE);
        response.setMessage(message);
        return response;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
