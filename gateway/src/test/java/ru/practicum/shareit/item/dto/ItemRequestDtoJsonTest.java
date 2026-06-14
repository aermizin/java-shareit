package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

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
    void deserialize_success() throws Exception {
        String content = "{ \"name\": \"Дрель\", \"description\": \"Аккумуляторная дрель\", \"available\": true, \"requestId\": 5 }";
        ItemRequestDto dto = json.parse(content).getObject();
        assertThat(dto.getName()).isEqualTo("Дрель");
        assertThat(dto.getDescription()).isEqualTo("Аккумуляторная дрель");
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getRequestId()).isEqualTo(5L);
    }
}
