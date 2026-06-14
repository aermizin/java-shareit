package ru.practicum.shareit.request.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestDtoValidationTest {

    @Autowired
    private JacksonTester<RequestDto> json;

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void serialize_success() throws Exception {
        RequestDto dto = new RequestDto("Аккумуляторная дрель");

        assertThat(json.write(dto))
                .hasJsonPathValue("$.description", "Аккумуляторная дрель");
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
