package ua.knu.knudev.iccsecurity.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
public class CorsFilter extends OncePerRequestFilter {

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList("http://localhost:3000", "https://example.com");

    private static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, PATCH, OPTIONS";
    private static final String ALLOWED_HEADERS = "Authorization,authorization,Content-Type,content-type";
    private static final String ALLOW_CREDENTIALS = "true";
    private static final long MAX_AGE = 3600;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String origin = request.getHeader("Origin");

        if (isAllowedOrigin(origin)) {

            //TODO change on specific origins * -> origin
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", ALLOWED_METHODS);
            response.setHeader("Access-Control-Allow-Headers", ALLOWED_HEADERS);
            response.setHeader("Access-Control-Allow-Credentials", ALLOW_CREDENTIALS);
            response.setHeader("Access-Control-Max-Age", String.valueOf(MAX_AGE));

            if ("OPTIONS".equalsIgnoreCase(request.getMethod()) &&
                    request.getHeader("Access-Control-Request-Method") != null) {
                handlePreflightRequest(request, response);
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CORS origin denied");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handlePreflightRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestedMethod = request.getHeader("Access-Control-Request-Method");
        String requestedHeaders = request.getHeader("Access-Control-Request-Headers");

        if (ALLOWED_METHODS.contains(requestedMethod) && areAllowedHeaders(requestedHeaders)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CORS preflight request");
        }
    }

    private boolean areAllowedHeaders(String headers) {
        if (headers == null || headers.isEmpty()) {
            return true;
        }
        List<String> requestedHeaders = Arrays.asList(headers.split(","));
        List<String> allowedHeaders = Arrays.asList(ALLOWED_HEADERS.split(","));

        return new HashSet<>(allowedHeaders).containsAll(requestedHeaders);
    }

    private boolean isAllowedOrigin(String origin) {
        if (origin == null) {
            return true;
        }

        return true;
//        TODO uncomment in prod
//        return ALLOWED_ORIGINS.contains(origin);
    }
}
