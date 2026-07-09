package com.quoteflow.exception;

import com.quoteflow.dto.ApiResponse;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST APIs.
 *
 * @author QuoteFlow
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles business exceptions.
     *
     * @param exception business exception
     * @return unified response
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> handleBusinessException(BusinessException exception) {
        LOGGER.warn("Business exception occurred, code={}, message={}",
                exception.getErrorCodeEnum().getCode(), exception.getMessage(), exception);
        return ApiResponse.fail(exception.getErrorCodeEnum().getCode(), exception.getMessage());
    }

    /**
     * Handles request validation exceptions.
     *
     * @param exception validation exception
     * @return unified response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));
        return ApiResponse.fail(ErrorCodeEnum.INVALID_PARAMETER.getCode(), message);
    }

    /**
     * Handles bind exceptions.
     *
     * @param exception bind exception
     * @return unified response
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBindException(BindException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));
        return ApiResponse.fail(ErrorCodeEnum.INVALID_PARAMETER.getCode(), message);
    }

    /**
     * Handles unexpected exceptions.
     *
     * @param exception unexpected exception
     * @return unified response
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception exception) {
        LOGGER.error("Unexpected exception occurred", exception);
        return ApiResponse.fail("500", "系统异常，请稍后重试");
    }

    private String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + " " + fieldError.getDefaultMessage();
    }
}
