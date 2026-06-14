package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserRequestDtoJsonTest {

    @Autowired
    private JacksonTester<UserRequestDto> json;

    @Test
    void serialize_success() throws Exception {
        UserRequestDto dto = new UserRequestDto(
                "Иван",
                "ivan@mail.ru"
        );

        assertThat(json.write(dto))
                .hasJsonPathValue("$.name", "Иван")
                .hasJsonPathValue("$.email", "ivan@mail.ru");
    }

    @Test
    void deserialize_success() throws Exception {
        String content = "{ \"name\": \"Иван\", \"email\": \"ivan@mail.ru\" }";
        UserRequestDto dto = json.parse(content).getObject();
        assertThat(dto.getName()).isEqualTo("Иван");
        assertThat(dto.getEmail()).isEqualTo("ivan@mail.ru");
    }
}
