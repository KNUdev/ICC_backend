package ua.knu.knudev.iccrest.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private int status;
    private Instant timestamp;
    private Object[] params;

    public static ErrorResponse of(String code, String message, int status) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .status(status)
                .timestamp(Instant.now())
                .params(new Object[0])
                .build();
    }

    public static ErrorResponse of(String code, String message, int status, Object... params) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .status(status)
                .timestamp(Instant.now())
                .params(params != null ? params : new Object[0])
                .build();
    }
}
