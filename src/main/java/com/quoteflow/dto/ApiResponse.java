package com.quoteflow.dto;

/**
 * 统一接口响应。
 *
 * @param <T> 响应数据类型
 * @author QuoteFlow
 */
public class ApiResponse<T> {

    /**
     * 响应编码。
     */
    private String code;

    /**
     * 请求是否成功。
     */
    private Boolean success;

    /**
     * 响应消息。
     */
    private String message;

    /**
     * 响应数据。
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
