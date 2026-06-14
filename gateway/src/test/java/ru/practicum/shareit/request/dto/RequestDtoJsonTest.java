package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestDtoJsonTest {

    @Autowired
    private JacksonTester<RequestDto> json;

    @Test
    void serialize_success() throws Exception {
        RequestDto dto = new RequestDto("Аккумуляторная дрель");

        assertThat(json.write(dto))
                .hasJsonPathValue("$.description", "Аккумуляторная дрель");
    }

    @Test
    void deserialize_success() throws Exception {
        String content = "{ \"description\": \"Аккумуляторная дрель\" }";
        RequestDto dto = json.parse(content).getObject();
        assertThat(dto.getDescription()).isEqualTo("Аккумуляторная дрель");
    }
}
