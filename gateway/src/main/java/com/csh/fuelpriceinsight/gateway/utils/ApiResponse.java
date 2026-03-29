package com.csh.fuelpriceinsight.gateway.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private int statusCode;
    private String path;
    private String timestamp;
    private T data;
    private ApiError error;

    @Setter
    @Getter
    public static class ApiError {
        private String errorCode;
        private String errorMessage;
        private Object fieldErrors;
    }
}
