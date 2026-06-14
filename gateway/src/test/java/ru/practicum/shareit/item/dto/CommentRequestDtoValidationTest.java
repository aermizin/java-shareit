package ru.practicum.shareit.item.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validation_success_whenFieldValid() {
        CommentRequestDto dto = new CommentRequestDto();
        dto.setText("Комментарий");
        var violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_fail_whenFieldIsNull() {
        CommentRequestDto dto = new CommentRequestDto();
        dto.setText(null);
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
    }

    @Test
    void validation_fail_whenFieldIsBlank() {
        CommentRequestDto dto = new CommentRequestDto();
        dto.setText("");
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
    }

    @Test
    void validation_fail_whenNameAndDescriptionAreOnlySpace() {
        CommentRequestDto dto = new CommentRequestDto();
        dto.setText("   ");
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
    }
}
