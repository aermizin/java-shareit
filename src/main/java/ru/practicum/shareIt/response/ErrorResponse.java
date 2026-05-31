
package ru.practicum.shareIt.response;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        List<ValidationError> validationErrors
) {
    public static ErrorResponse of(LocalDateTime timestamp, int status, String error, String message) {
        return new ErrorResponse(timestamp, status, error, message, null);
    }

    public static ErrorResponse ofValidationError(LocalDateTime timestamp, int status, String error, String message,
                                                  List<ValidationError> validationErrors) {
        return new ErrorResponse(timestamp, status, error, message, validationErrors);
    }
}
