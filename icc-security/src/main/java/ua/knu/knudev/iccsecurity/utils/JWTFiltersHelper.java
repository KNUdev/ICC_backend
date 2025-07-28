package ua.knu.knudev.iccsecurity.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JWTFiltersHelper {

    public void writeMessageInResponse(HttpServletResponse response,
                                       int statusCode,
                                       String message) throws IOException {
        response.setStatus(statusCode);
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(message);
    }

    public String extractJWTHeader(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

}

