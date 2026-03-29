package com.csh.fuelpriceinsight.gateway.filter;

import com.csh.fuelpriceinsight.gateway.utils.FiltersOrder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.*;

@Component
@Order(FiltersOrder.LOGGING)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger requestLogger = LoggerFactory.getLogger("RequestResponseLogger");

    @Value("${gateway.logging.request-cache-limit}")
    private int requestCacheLimit;

    private static final Set<String> SENSITIVE_HEADERS = Set.of(
            "authorization", "cookie", "set-cookie",
            "x-api-key", "x-auth-token", "proxy-authorization"
    );

    private static final Set<String> SAFE_HEADERS = Set.of(
            "content-type", "accept", "user-agent", "referer",
            "origin", "x-request-id", "x-forwarded-for", "x-correlation-id"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request, requestCacheLimit);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        String correlationId = Optional
                .ofNullable(request.getHeader("x-correlation-id"))
                .orElse(UUID.randomUUID().toString());

        wrappedResponse.addHeader("x-correlation-id", correlationId);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);

            long duration = System.currentTimeMillis() - startTime;
            int status = wrappedResponse.getStatus();
            logEntry(wrappedRequest, correlationId, status, duration, null);

        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - startTime;
            logEntry(wrappedRequest, correlationId, 500, duration, ex.getMessage());
            throw ex;

        } finally {
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logEntry(HttpServletRequest request, String correlationId,
                          int status, long duration, String errorMessage) {
        String log = buildLog(request, correlationId, status, duration, errorMessage);

        if (status >= 500) {
            requestLogger.error(log);
        } else if (status >= 400) {
            requestLogger.warn(log);
        } else {
            requestLogger.info(log);
        }
    }

    private String buildLog(HttpServletRequest request, String correlationId,
                            int status, long duration, String errorMessage) {

        StringBuilder log = new StringBuilder();
        log.append("\n========== GATEWAY REQUEST ==========\n");
        log.append(String.format("  Correlation ID : %s%n", correlationId));
        log.append(String.format("  Method         : %s%n", request.getMethod()));
        log.append(String.format("  URI            : %s%n", request.getRequestURI()));
        log.append(String.format("  Path           : %s%n", request.getServletPath()));
        log.append(String.format("  Query Params   : %s%n", request.getQueryString()));
        log.append(String.format("  Client IP      : %s%n", getClientIp(request)));
        log.append(String.format("  Safe Headers   : %s%n", getSafeHeaders(request)));
        log.append("========== GATEWAY RESPONSE ==========\n");
        log.append(String.format("  Status         : %d%n", status));
        log.append(String.format("  Duration       : %d ms%n", duration));
        if (errorMessage != null) {
            log.append("========== ERROR ==========\n");
            log.append(String.format("  Error          : %s%n", errorMessage));
        }
        log.append("======================================");
        return log.toString();
    }

    private String getSafeHeaders(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder("{");
        Collections.list(request.getHeaderNames()).forEach(name -> {
            String lower = name.toLowerCase();
            if (SENSITIVE_HEADERS.contains(lower)) {
                sb.append(name).append("=[REDACTED], ");
            } else if (SAFE_HEADERS.contains(lower)) {
                sb.append(name).append("=").append(request.getHeader(name)).append(", ");
            }
        });
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return Optional.ofNullable(request.getRemoteAddr()).orElse("unknown");
    }
}
