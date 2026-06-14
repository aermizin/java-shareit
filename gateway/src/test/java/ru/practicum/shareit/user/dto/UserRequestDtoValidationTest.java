package ru.practicum.shareit.user.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validation_success_whenFieldsValid() {
        UserRequestDto dto = new UserRequestDto();
        dto.setName("Иван");
        dto.setEmail("ivan@gmail.com");

        var violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_fail_whenFieldsIsNull() {
        UserRequestDto dto = new UserRequestDto();
        dto.setName(null);
        dto.setEmail(null);
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(2);
    }

    @Test
    void validation_fail_whenFieldsIsBlank() {
        UserRequestDto dto = new UserRequestDto();
        dto.setName("");
        dto.setEmail("");
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(2);
    }

    @Test
    void validation_fail_whenNameIsOnlySpace() {
        UserRequestDto dto = new UserRequestDto();
        dto.setName("   ");
        dto.setEmail("ivan@gmail.com");
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
    }

    @Test
    void validation_fail_whenEmailInValid() {
        UserRequestDto dto = new UserRequestDto();
        dto.setName("Иван");
        dto.setEmail("ivangmail.com");
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
    }
}
