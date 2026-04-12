package com.csh.fuelpriceinsight.gateway.filter;

import com.csh.fuelpriceinsight.gateway.utils.CustomRequestWrapper;
import com.csh.fuelpriceinsight.gateway.utils.FiltersOrder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(FiltersOrder.USER_HEADER)
public class UserHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthenticationToken jwtAuth) {

            Jwt jwt = jwtAuth.getToken();

            String userId = jwt.getSubject();
            String username = jwt.getClaim("preferred_username");

            CustomRequestWrapper requestWrapper = new CustomRequestWrapper(request);
            requestWrapper.addHeader("X-User-Id", userId);
            requestWrapper.addHeader("X-Username", username);

            filterChain.doFilter(requestWrapper, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}

