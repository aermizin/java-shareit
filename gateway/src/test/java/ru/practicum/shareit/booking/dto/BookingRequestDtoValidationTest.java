package ru.practicum.shareit.booking.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

public class BookingRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validation_success_whenDatesValid() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(Instant.now().plusSeconds(5));
        dto.setEnd(Instant.now().plusSeconds(10));
        var violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_fail_whenStartInPast() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(Instant.now().minusSeconds(5));
        dto.setEnd(Instant.now().plusSeconds(10));
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
    }

    @Test
    void validation_fail_whenEndNotInFuture() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(Instant.now().plusSeconds(5));
        dto.setEnd(Instant.now().minusSeconds(5));
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
    }

    @Test
    void validation_fail_whenStartAndEndNotInFuture() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(Instant.now().minusSeconds(5));
        dto.setEnd(Instant.now().minusSeconds(5));
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(2);
    }
}
