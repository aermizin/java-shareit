package ru.practicum.shareit.request.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestDtoValidationTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validation_success_whenFieldValid() {
        RequestDto dto = new RequestDto();
        dto.setDescription("Описание");
        var violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_fail_whenFieldIsNull() {
        RequestDto dto = new RequestDto();
        dto.setDescription(null);
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
    }

    @Test
    void validation_fail_whenFieldIsBlank() {
        RequestDto dto = new RequestDto();
        dto.setDescription("");
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
    }

    @Test
    void validation_fail_whenDescriptionIsOnlySpace() {
        RequestDto dto = new RequestDto();
        dto.setDescription("   ");
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
    }
}
