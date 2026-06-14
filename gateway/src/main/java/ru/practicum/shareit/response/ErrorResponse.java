package ru.practicum.shareit.response;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant  timestamp,
        int status,
        String error,
        String message,
        List<ValidationError> validationErrors
) {
    public static ErrorResponse of(Instant timestamp, int status, String error, String message) {
        return new ErrorResponse(timestamp, status, error, message, null);
    }

    public static ErrorResponse ofValidationError(Instant timestamp, int status, String error, String message,
                                                  List<ValidationError> validationErrors) {
        return new ErrorResponse(timestamp, status, error, message, validationErrors);
    }
}
