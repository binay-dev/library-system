package com.library.api.infrastructure.exception;
import com.library.api.application.response.ApiResponse;
import com.library.api.domain.exception.DomainException;
import com.library.api.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainException(DomainException ex) {
        ErrorCode error = ex.getErrorCode();

        // ✨ Map pure domain error tracking codes to concrete Spring HTTP status codes here
        HttpStatus httpStatus = switch (error) {
            case BORROWER_NOT_FOUND, BOOK_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DUPLICATE_EMAIL, ISBN_CONFLICT -> HttpStatus.CONFLICT;
            case INVALID_REQUEST -> HttpStatus.BAD_REQUEST;
        };

        return ResponseEntity
                .status(httpStatus)
                .body(ApiResponse.fail(
                        error.getCode(),
                        ex.getMessage() != null ? ex.getMessage() : error.getMessage()
                ));
    }

    /**
     * Optional: Add a fallback for generic runtime system anomalies
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(
                        "INTERNAL_SERVER_ERROR",
                        "An unexpected processing error occurred on the server."
                ));
    }
}