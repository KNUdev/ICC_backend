package ua.knu.knudev.iccrest.utils;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ValidationErrorResponse {
    private String errorCode;
    private String message;
    private int status;
    private Map<String, String> fieldErrors;

    public static ValidationErrorResponse of(String errorCode, String message, int status, Map<String, String> fieldErrors) {
        return ValidationErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .status(status)
                .fieldErrors(fieldErrors)
                .build();
    }
}