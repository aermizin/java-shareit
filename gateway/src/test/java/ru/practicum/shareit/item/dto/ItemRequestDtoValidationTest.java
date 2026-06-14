package ru.practicum.shareit.item.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoValidationTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void serialize_success() throws Exception {
        ItemRequestDto dto = new ItemRequestDto(
                "Дрель",
                "Аккумуляторная дрель",
                true,
                5L
        );

        assertThat(json.write(dto))
                .hasJsonPathValue("$.name", "Дрель")
                .hasJsonPathValue("$.description", "Аккумуляторная дрель")
                .hasJsonPathValue("$.available", true)
                .hasJsonPathValue("$.requestId", 5);
    }

    @Test
    void validation_success_whenFieldsValid() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setName("Дрель");
        dto.setDescription("Электрическая");
        dto.setAvailable(true);
        dto.setRequestId(null);
        var violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_fail_whenFieldsIsNull() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setName(null);
        dto.setDescription(null);
        dto.setAvailable(null);
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(3);
    }

    @Test
    void validation_fail_whenNameAndDescriptionIsBlank() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setName("");
        dto.setDescription("");
        dto.setAvailable(true);
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(2);
    }

    @Test
    void validation_fail_whenNameAndDescriptionAreOnlySpaces() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setName("   ");
        dto.setDescription("   ");
        dto.setAvailable(true);
        var violations = validator.validate(dto);
        assertThat(violations).hasSize(2);
    }
}