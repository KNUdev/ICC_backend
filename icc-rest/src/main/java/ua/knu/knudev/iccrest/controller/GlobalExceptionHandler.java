package ua.knu.knudev.iccrest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.knu.knudev.employeemanagerapi.exception.EmployeeException;
import ua.knu.knudev.employeemanagerapi.exception.SectorException;
import ua.knu.knudev.employeemanagerapi.exception.SpecialtyException;
import ua.knu.knudev.fileserviceapi.exception.GalleryItemException;
import ua.knu.knudev.icccommon.exception.FileException;
import ua.knu.knudev.iccrest.utils.ErrorResponse;
import ua.knu.knudev.iccsecurity.exception.AccountAuthException;
import ua.knu.knudev.iccsecurity.exception.TokenException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> createErrorResponse(String errorCode, String message, int status) {
        ErrorResponse errorResponse = ErrorResponse.of(
                errorCode,
                message,
                status
        );
        return ResponseEntity.status(HttpStatus.valueOf(status)).body(errorResponse);
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
        ErrorResponse errorResponse = ErrorResponse.of(
                "GALLERY_ITEM_EXCEPTION",
                exception.getMessage(),
                400
        );

        return ResponseEntity.status(HttpStatus.valueOf(400)).body(errorResponse);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<ErrorResponse> handleFileException(FileException exception) {
        ErrorResponse errorResponse = ErrorResponse.of(
                "FILE_EXCEPTION",
                exception.getMessage(),
                400
        );

        return ResponseEntity.status(HttpStatus.valueOf(400)).body(errorResponse);
    }

    @ExceptionHandler(AccountAuthException.class)
    public ResponseEntity<ErrorResponse> handleAccountAuthException(AccountAuthException exception) {
        ErrorResponse errorResponse = ErrorResponse.of(
                "ACCOUNT_AUTH_EXCEPTION",
                exception.getMessage(),
                400
        );

        return ResponseEntity.status(HttpStatus.valueOf(400)).body(errorResponse);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> handleTokenException(TokenException exception) {
        ErrorResponse errorResponse = ErrorResponse.of(
                "TOKEN_EXCEPTION",
                exception.getMessage(),
                400
        );

        return ResponseEntity.status(HttpStatus.valueOf(400)).body(errorResponse);
    }

}
