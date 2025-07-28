package ua.knu.knudev.iccsecurity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class TokenException extends RuntimeException {
    private HttpStatus statusCode;

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
