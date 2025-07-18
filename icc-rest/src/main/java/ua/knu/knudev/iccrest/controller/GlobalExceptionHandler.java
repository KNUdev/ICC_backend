package ua.knu.knudev.iccrest.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.knu.knudev.applicationmanagerapi.exception.ApplicationException;
import ua.knu.knudev.applicationmanagerapi.exception.DepartmentException;
import ua.knu.knudev.employeemanagerapi.exception.EmployeeException;
import ua.knu.knudev.employeemanagerapi.exception.SectorException;
import ua.knu.knudev.employeemanagerapi.exception.SpecialtyException;
import ua.knu.knudev.fileserviceapi.exception.GalleryItemException;
import ua.knu.knudev.icccommon.exception.FileException;
import ua.knu.knudev.iccrest.utils.ErrorResponse;
import ua.knu.knudev.iccrest.utils.ValidationErrorResponse;
import ua.knu.knudev.iccsecurity.exception.AccountAuthException;
import ua.knu.knudev.iccsecurity.exception.TokenException;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ValidationErrorResponse errorResponse = ValidationErrorResponse.of(
                "VALIDATION_ERROR",
                "Validation failed",
                HttpStatus.BAD_REQUEST.value(),
                errors
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(
            ConstraintViolationException exception) {

        Map<String, String> errors = exception.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        this::getPropertyPath,
                        ConstraintViolation::getMessage
                ));

        ValidationErrorResponse errorResponse = ValidationErrorResponse.of(
                "VALIDATION_ERROR",
                "Validation failed",
                HttpStatus.BAD_REQUEST.value(),
                errors
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleException(RuntimeException exception) {
        return createErrorResponse("RUNTIME_EXCEPTION", exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(EmployeeException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeException(EmployeeException exception) {
        return createErrorResponse("EMPLOYEE_EXCEPTION", exception.getMessage(), 400);
    }

    @ExceptionHandler(SectorException.class)
    public ResponseEntity<ErrorResponse> handleSectorException(SectorException exception) {
        return createErrorResponse("SECTOR_EXCEPTION", exception.getMessage(), 400);
    }

    @ExceptionHandler(SpecialtyException.class)
    public ResponseEntity<ErrorResponse> handleSpecialtyException(SpecialtyException exception) {
        return createErrorResponse("SPECIALTY_EXCEPTION", exception.getMessage(), 400);
    }

    @ExceptionHandler(GalleryItemException.class)
    public ResponseEntity<ErrorResponse> handleGalleryItemException(GalleryItemException exception) {
        return createErrorResponse("GALLERY_ITEM_EXCEPTION", exception.getMessage(), 400);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<ErrorResponse> handleFileException(FileException exception) {
        return createErrorResponse("FILE_EXCEPTION", exception.getMessage(), 400);
    }

    @ExceptionHandler(AccountAuthException.class)
    public ResponseEntity<ErrorResponse> handleAccountAuthException(AccountAuthException exception) {
        return createErrorResponse("ACCOUNT_AUTH_EXCEPTION", exception.getMessage(), 400);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> handleTokenException(TokenException exception) {
        return createErrorResponse("TOKEN_EXCEPTION", exception.getMessage(), 400);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception) {
        return createErrorResponse("APPLICATION_EXCEPTION", exception.getMessage(), 400);
    }

    @ExceptionHandler(DepartmentException.class)
    public ResponseEntity<ErrorResponse> handleDepartmentException(DepartmentException exception) {
        return createErrorResponse("DEPARTMENT_EXCEPTION", exception.getMessage(), 400);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurityException(SecurityException exception) {
        return createErrorResponse("SECURITY_EXCEPTION", exception.getMessage(), 400);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorResponse> handleLoginException(LoginException exception) {
        return createErrorResponse("LOGIN_EXCEPTION", exception.getMessage(), 400);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(DisabledException exception) {
        return createErrorResponse("DISABLED_EXCEPTION", exception.getMessage(), 400);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLockedException(LockedException exception) {
        return createErrorResponse("LOCKED_EXCEPTION", exception.getMessage(), 400);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(
            String errorCode,
            String message,
            int status
    ) {
        ErrorResponse errorResponse = ErrorResponse.of(
                errorCode,
                message,
                status
        );
        return ResponseEntity.status(HttpStatus.valueOf(status)).body(errorResponse);
    }

    private String getPropertyPath(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        int lastDotIndex = propertyPath.lastIndexOf('.');
        return lastDotIndex >= 0 ? propertyPath.substring(lastDotIndex + 1) : propertyPath;
    }

}
