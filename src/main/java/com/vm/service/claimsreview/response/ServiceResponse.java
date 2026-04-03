package com.vm.service.claimsreview.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Generic service response wrapper.
 * Encapsulates the result of service operations including status and data.
 * Allows services to communicate results without HTTP dependencies.
 */
@Data
@AllArgsConstructor
public class ServiceResponse<T> {
    
    private boolean success;
    private T data;
    private String errorMessage;
    private HttpStatus httpStatus;

    /**
     * Create successful response
     */
    public static <T> ServiceResponse<T> success(T data) {
        return new ServiceResponse<>(true, data, null, HttpStatus.OK);
    }

    /**
     * Create successful response with custom status
     */
    public static <T> ServiceResponse<T> success(T data, HttpStatus status) {
        return new ServiceResponse<>(true, data, null, status);
    }

    /**
     * Create error response
     */
    public static <T> ServiceResponse<T> error(String errorMessage, HttpStatus status) {
        return new ServiceResponse<>(false, null, errorMessage, status);
    }

    /**
     * Create validation error response
     */
    public static <T> ServiceResponse<T> validationError(String errorMessage) {
        return new ServiceResponse<>(false, null, errorMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Create internal error response
     */
    public static <T> ServiceResponse<T> internalError(String errorMessage) {
        return new ServiceResponse<>(false, null, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
