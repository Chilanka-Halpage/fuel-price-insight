package com.csh.fuelpriceinsight.gateway.filter;


import com.csh.fuelpriceinsight.gateway.utils.ApiResponse;
import com.csh.fuelpriceinsight.gateway.utils.FiltersOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(FiltersOrder.API_RESPONSE)
public class ApiResponseFilter implements Filter {

    private final ObjectMapper objectMapper;

    public ApiResponseFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper(response);

        chain.doFilter(request, responseWrapper);

        byte[] originalBody = responseWrapper.getContentAsByteArray();
        String originalBodyStr = new String(originalBody, StandardCharsets.UTF_8);
        int statusCode = responseWrapper.getStatus();
        String path = request.getRequestURI();

        String wrappedResponse;

        if (statusCode >= 200 && statusCode < 300) {
            wrappedResponse = buildSuccess(originalBodyStr, statusCode, path);
        } else {
            wrappedResponse = buildError(originalBodyStr, statusCode, path);
        }

        byte[] wrappedBytes = wrappedResponse.getBytes(StandardCharsets.UTF_8);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setContentLength(wrappedBytes.length);
        response.getOutputStream().write(wrappedBytes);
    }

    private String buildSuccess(String body, int statusCode, String path) {
        try {
            Object data = objectMapper.readValue(body, Object.class);

            ApiResponse<Object> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage(resolveSuccessMessage(statusCode));
            response.setStatusCode(statusCode);
            response.setTimestamp(LocalDateTime.now().toString());
            response.setPath(path);
            response.setData(data);
            response.setError(null);

            return objectMapper.writeValueAsString(response);

        } catch (Exception e) {
            return fallback(statusCode, path);
        }
    }

    private String buildError(String body, int statusCode, String path) {
        try {
            Map<String, Object> errorMap = new HashMap<>();
            try {
                errorMap = objectMapper.readValue(body, new TypeReference<>() {});
            } catch (Exception ignored) {}

            ApiResponse.ApiError apiError = new ApiResponse.ApiError();
            apiError.setErrorCode((String) errorMap.getOrDefault("errorCode", "UNKNOWN_ERROR"));
            apiError.setErrorMessage((String) errorMap.getOrDefault("errorMessage", "An error occurred"));
            apiError.setFieldErrors(errorMap.getOrDefault("fieldErrors", null));

            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage(apiError.getErrorMessage());
            response.setStatusCode(statusCode);
            response.setTimestamp(LocalDateTime.now().toString());
            response.setPath(path);
            response.setData(null);
            response.setError(apiError);

            return objectMapper.writeValueAsString(response);

        } catch (Exception e) {
            return fallback(statusCode, path);
        }
    }

    private String fallback(int statusCode, String path) {
        return String.format(
                "{\"success\":false,\"message\":\"Gateway error\"," +
                        "\"statusCode\":%d,\"path\":\"%s\",\"data\":null,\"error\":null}",
                statusCode, path
        );
    }

    private String resolveSuccessMessage(int statusCode) {
        return switch (statusCode) {
            case 201 -> "Resource created successfully";
            case 204 -> "Operation completed";
            default  -> "Request processed successfully";
        };
    }
}
